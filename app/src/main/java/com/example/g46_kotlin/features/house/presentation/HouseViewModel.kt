package com.example.g46_kotlin.features.house.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.core.domain.contract.NotificationPublisher
import com.example.g46_kotlin.core.location.CurrentLocationSource
import com.example.g46_kotlin.features.analytics.data.repository.AnalyticsRepository
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.PropertySortBy
import com.example.g46_kotlin.features.house.domain.model.SearchPropertiesFilters
import com.example.g46_kotlin.features.house.domain.usecase.GetHouseUseCase
import com.example.g46_kotlin.features.house.domain.usecase.GetNearestAvailablePropertyUseCase
import com.example.g46_kotlin.features.favorites.domain.usecase.GetFavoriteIdsUseCase
import com.example.g46_kotlin.features.favorites.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.g46_kotlin.features.house.domain.model.PagedProperties
import com.example.g46_kotlin.core.domain.contract.NetworkMonitor
import com.example.g46_kotlin.features.house.data.local.SharedPrefsPageCache
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn



@HiltViewModel
class HouseViewModel @Inject constructor(
    private val notificationPublisher: NotificationPublisher,
    private val getHouseUseCase: GetHouseUseCase,
    private val currentLocationSource: CurrentLocationSource,
    private val getNearestAvailablePropertyUseCase: GetNearestAvailablePropertyUseCase,
    private val analyticsRepository: AnalyticsRepository,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val networkMonitor: NetworkMonitor,
    private val pageCache: SharedPrefsPageCache
) : ViewModel() {
    private val _uiState = MutableStateFlow(HouseUiState())
    val uiState: StateFlow<HouseUiState> = _uiState.asStateFlow()
    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    private var lastLoadFailed = false

    private var lastNotifiedPropertyId: String? = null
    private var lastTrackedRadiusKm: Double? = null

    private companion object {
        const val MIN_SAMPLES_FOR_GLOBAL_INSIGHT = 5
        const val INSIGHT_LOOKBACK_DAYS = 30L
    }

    init {
        loadHouses()
        loadGlobalDistanceInsight()
        observeConnectivity()

        viewModelScope.launch {
            getFavoriteIdsUseCase().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
            }
        }
    }

    fun loadHouses() {
        searchHouses()
    }

    fun onNeighborhoodChange(value: String) {
        _uiState.update {
            it.copy(
                neighborhoodQuery = value,
                page = 1
            )
        }
    }

    fun onBudgetClick(option: String) {
        _uiState.update {
            it.copy(
                selectedBudget = if (it.selectedBudget == option) null else option,
                page = 1
            )
        }
    }

    fun onMinBedroomsClick(value: Int) {
        _uiState.update {
            it.copy(
                selectedMinBedrooms = if (it.selectedMinBedrooms == value) null else value,
                page = 1
            )
        }
    }

    fun onFurnishedToggle() {
        _uiState.update { state ->
            state.copy(
                furnished = when (state.furnished) {
                    null -> true
                    true -> null
                    false -> null
                },
                page = 1
            )
        }
    }

    fun onPetFriendlyToggle() {
        _uiState.update { state ->
            state.copy(
                petFriendly = when (state.petFriendly) {
                    null -> true
                    true -> null
                    false -> null
                },
                page = 1
            )
        }
    }

    fun onSortByDistanceToggle() {
        _uiState.update {
            it.copy(
                sortByDistance = !it.sortByDistance,
                page = 1
            )
        }
    }

    fun onRadiusKmChange(value: String) {
        val sanitized = value.filter { it.isDigit() || it == '.' }
        _uiState.update {
            it.copy(
                radiusKmInput = sanitized,
                page = 1
            )
        }
    }

    fun onSearchSubmitted() {
        searchHouses()
    }

    fun onNextPage() {
        if (_uiState.value.page < _uiState.value.totalPages) {
            _uiState.update { it.copy(page = it.page + 1) }
            searchHouses()
        }
    }

    fun onPrevPage() {
        if (_uiState.value.page > 1) {
            _uiState.update { it.copy(page = it.page - 1) }
            searchHouses()
        }
    }

    fun onApplyDistanceRecommendation() {
        val insight = _uiState.value.globalDistanceInsight ?: return
        val recommended = insight.recommendedMaxKm ?: return

        _uiState.update {
            it.copy(
                radiusKmInput = formatRadiusInput(recommended),
                page = 1
            )
        }

        viewModelScope.launch {
            val snapshot = _uiState.value
            val filters = buildFilters(snapshot)

            runCatching {
                analyticsRepository.trackDistanceRecommendationApplied(
                    recommendedRadiusKm = recommended,
                    lat = filters.lat,
                    lng = filters.lng,
                    neighborhood = filters.neighborhood
                )
            }
        }
    }

    private fun searchHouses() {
        viewModelScope.launch {
            val snapshot = _uiState.value
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            if (!isConnected.value) {
                val cached = pageCache.getPage(snapshot.page)
                val cachedNums = pageCache.getCachedPageNumbers()
                if (cached != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            houses = cached,
                            isOffline = true,
                            offlineMessage = "Sin conexión – mostrando las últimas ${cachedNums.size} páginas guardadas",
                            cachedPageNumbers = cachedNums,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isOffline = true,
                            houses = emptyList(),
                            offlineMessage = "Sin conexión – esta página no está guardada",
                            cachedPageNumbers = cachedNums,
                            errorMessage = null
                        )
                    }
                }
                return@launch
            }

            val filters = buildFilters(snapshot)

            runCatching {
                getHouseUseCase(filters)
            }.onSuccess { pagedResult ->
                val userLocation = currentLocationSource.getCurrentLocationOrNull()
                val nearest = userLocation?.let { location ->
                    getNearestAvailablePropertyUseCase(location, pagedResult.properties)
                }

                nearest?.let { result ->
                    val shouldNotify = lastNotifiedPropertyId != result.property.id
                    if (shouldNotify) {
                        runCatching {
                            notificationPublisher.publishContextAware(
                                propertyTitle = result.property.title,
                                neighborhood = result.property.neighborhood,
                                distanceMeters = result.distanceMeters,
                                propertyId = result.property.id,
                                propertyImage = result.property.imageUrls.firstOrNull() ?: ""
                            )
                        }.onSuccess { published ->
                            if (published) lastNotifiedPropertyId = result.property.id
                        }
                    }
                }

                val mapped = pagedResult.properties.map { property -> property.toHousingCardUi() }
                pageCache.savePage(snapshot.page, mapped)
                val cachedNums = pageCache.getCachedPageNumbers()

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        houses = mapped,
                        allProperties = pagedResult.properties,
                        totalPages = pagedResult.totalPages,
                        isOffline = false,
                        offlineMessage = null,
                        cachedPageNumbers = cachedNums,
                        errorMessage = null,
                        lastActionMessage = nearest?.let { result ->
                            "Cerca de ti: ${result.property.title} a ${formatDistance(result.distanceMeters)}"
                        }
                    )
                }

                lastLoadFailed = false

                val currentRadiusKm = snapshot.radiusKmInput.toDoubleOrNull()?.takeIf { it > 0.0 }

                launch {
                    runCatching {
                        analyticsRepository.trackSearchSubmitted(
                            filters = filters,
                            neighborhoodQuery = snapshot.neighborhoodQuery
                        )
                    }
                }

                if (currentRadiusKm != lastTrackedRadiusKm) {
                    lastTrackedRadiusKm = currentRadiusKm
                    launch {
                        runCatching {
                            analyticsRepository.trackDistanceFilterChanged(
                                radiusKm = currentRadiusKm,
                                lat = filters.lat,
                                lng = filters.lng,
                                neighborhood = filters.neighborhood
                            )
                        }
                    }
                }
            }.onFailure { error ->
                lastLoadFailed = true
                val cached = pageCache.getPage(snapshot.page)
                val cachedNums = pageCache.getCachedPageNumbers()
                if (cached != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            houses = cached,
                            isOffline = true,
                            offlineMessage = "Error de red – mostrando página guardada",
                            cachedPageNumbers = cachedNums,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            houses = emptyList(),
                            allProperties = emptyList(),
                            errorMessage = error.message ?: "Error cargando propiedades"
                        )
                    }
                }
            }
        }
    }

    private fun loadGlobalDistanceInsight() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGlobalDistanceInsightLoading = true,
                    globalDistanceInsight = null,
                    globalDistanceInsightFallbackMessage = null
                )
            }

            val now = System.currentTimeMillis()
            val from = now - (INSIGHT_LOOKBACK_DAYS * 24L * 60L * 60L * 1000L)

            val fromIso = toIsoUtc(from)
            val toIso = toIsoUtc(now)

            runCatching {
                analyticsRepository.getPreferredMaxDistanceSummary(
                    fromIso = fromIso,
                    toIso = toIso
                )
            }.onSuccess { data ->
                if (data == null || data.samples < MIN_SAMPLES_FOR_GLOBAL_INSIGHT) {
                    _uiState.update {
                        it.copy(
                            isGlobalDistanceInsightLoading = false,
                            globalDistanceInsight = null,
                            globalDistanceInsightFallbackMessage = "Aun no hay suficientes datos globales para recomendar una distancia."
                        )
                    }
                    return@onSuccess
                }

                val recommended = data.recommendedMaxKm

                val bannerMessage = if (recommended != null) {
                    "La distancia maxima preferida por la mayoria de usuarios es ${formatRadiusInput(recommended)} km."
                } else {
                    "No hay suficientes datos para calcular la distancia maxima preferida."
                }

                _uiState.update {
                    it.copy(
                        isGlobalDistanceInsightLoading = false,
                        globalDistanceInsight = GlobalDistanceInsightUi(
                            preferredKm = data.preferredKm,
                            recommendedMaxKm = data.recommendedMaxKm,
                            maxObservedKm = data.maxObservedKm,
                            samples = data.samples,
                            lastUpdatedAt = data.lastUpdatedAt,
                            message = bannerMessage
                        ),
                        globalDistanceInsightFallbackMessage = null
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isGlobalDistanceInsightLoading = false,
                        globalDistanceInsight = null,
                        globalDistanceInsightFallbackMessage = "No pudimos cargar la recomendacion global por ahora."
                    )
                }
            }
        }
    }

    private suspend fun buildFilters(state: HouseUiState): SearchPropertiesFilters {
        val radiusKm = state.radiusKmInput.toDoubleOrNull()?.takeIf { it > 0.0 }
        val wantsDistanceContext = state.sortByDistance || radiusKm != null

        val location = if (wantsDistanceContext) {
            currentLocationSource.getCurrentLocationOrNull()
        } else {
            null
        }

        val hasLatLng = location != null

        return SearchPropertiesFilters(
            maxPrice = budgetToMaxPrice(state.selectedBudget),
            minBedrooms = state.selectedMinBedrooms,
            furnished = state.furnished,
            petFriendly = state.petFriendly,
            neighborhood = state.neighborhoodQuery.trim().takeIf { it.isNotEmpty() },
            lat = if (hasLatLng) location!!.lat else null,
            lng = if (hasLatLng) location!!.lon else null,
            radiusKm = if (hasLatLng) radiusKm else null,
            sortBy = if (state.sortByDistance && hasLatLng) PropertySortBy.DISTANCE else null,
            page = state.page,
            limit = state.limit
        )
    }

    private fun budgetToMaxPrice(option: String?): Int? {
        return when (option) {
            "<=1.2M" -> 1_200_000
            "<=1.8M" -> 1_800_000
            "<=2.5M" -> 2_500_000
            else -> null
        }
    }

    fun onAvailabilityClick(houseName: String) {
        _uiState.update {
            it.copy(lastActionMessage = "Ver disponibilidad de $houseName")
        }
    }

    private fun PropertyDetail.toHousingCardUi(): HousingCardUi {
        val typeLabel = propertyType.name.replace("_", " ")
        val roomLabel = "$bedrooms Bed · $bathrooms Bath"

        return HousingCardUi(
            id = id,
            name = title,
            pricePerMonth = monthlyRent.toInt(),
            rating = 0.0,
            neighborhood = neighborhood,
            propertyType = "$typeLabel · $roomLabel",
            imageUrl = imageUrls.firstOrNull()
        )
    }

    @SuppressLint("DefaultLocale")
    private fun formatDistance(distanceMeters: Int): String {
        return if (distanceMeters < 1000) {
            "$distanceMeters m"
        } else {
            String.format("%.1f km", distanceMeters / 1000.0)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatRadiusInput(value: Double): String {
        return String.format("%.1f", value)
    }

    private fun toIsoUtc(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(millis))
    }

    fun onToggleFavorite(card: HousingCardUi) {
        viewModelScope.launch {
            toggleFavoriteUseCase(card)
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            isConnected.collect { connected ->
                if (!connected && !_uiState.value.isOffline) {
                    val cachedNums = pageCache.getCachedPageNumbers()
                    _uiState.update {
                        it.copy(
                            isOffline = true,
                            offlineMessage = "Sin conexión – mostrando las últimas ${cachedNums.size} páginas guardadas",
                            cachedPageNumbers = cachedNums
                        )
                    }
                } else if (connected && _uiState.value.isOffline) {
                    searchHouses()
                }
            }
        }
    }
}