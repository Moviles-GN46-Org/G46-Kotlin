package com.example.g46_kotlin.features.house.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.house.domain.usecase.GetHouseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.g46_kotlin.core.location.CurrentLocationSource
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.usecase.GetNearestAvailablePropertyUseCase
import java.util.UUID


@HiltViewModel
class HouseViewModel @Inject constructor(
    private val getHouseUseCase: GetHouseUseCase,
    private val currentLocationSource: CurrentLocationSource,
    private val getNearestAvailablePropertyUseCase: GetNearestAvailablePropertyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseUiState())
    val uiState: StateFlow<HouseUiState> = _uiState.asStateFlow()

    init {
        loadHouses()
    }

    fun loadHouses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                getHouseUseCase()
            }.onSuccess { properties ->
                val userLocation = currentLocationSource.getCurrentLocationOrNull()
                val nearest = userLocation?.let { location ->
                    getNearestAvailablePropertyUseCase(location, properties)
                }

                val newNotification = nearest?.let { result ->
                    HouseInAppNotification(
                        id = UUID.randomUUID().toString(),
                        title = "Propiedad cerca de ti",
                        message = "${result.property.title} en ${result.property.neighborhood} a ${formatDistance(result.distanceMeters)}",
                        createdAtMillis = System.currentTimeMillis(),
                        isRead = false
                    )
                }

                val mapped = properties.map { property -> property.toHousingCardUi() }

                _uiState.update { state ->
                    val updatedNotifications = if (newNotification != null) {
                        listOf(newNotification) + state.notifications
                    } else {
                        state.notifications
                    }

                    state.copy(
                        isLoading = false,
                        houses = mapped,
                        visibleHouses = mapped,
                        allProperties = properties,
                        notifications = updatedNotifications,
                        lastActionMessage = nearest?.let { result ->
                            "Cerca de ti: ${result.property.title} a ${formatDistance(result.distanceMeters)}"
                        }
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        houses = emptyList(),
                        visibleHouses = emptyList(),
                        errorMessage = error.message ?: "Error cargando propiedades"
                    )
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        _uiState.update { it.copy(query = value) }
        applyUiOnlyFilters()
    }

    fun onBudgetClick(option: String) {
        _uiState.update {
            it.copy(selectedBudget = if (it.selectedBudget == option) null else option)
        }
        applyUiOnlyFilters()
    }

    fun onRoomTypeClick(option: String) {
        _uiState.update {
            it.copy(selectedRoomType = if (it.selectedRoomType == option) null else option)
        }
        applyUiOnlyFilters()
    }

    fun onAmenityClick(option: String) {
        _uiState.update { current ->
            val updated = current.selectedAmenities.toMutableSet()
            if (updated.contains(option)) {
                updated.remove(option)
            } else {
                updated.add(option)
            }
            current.copy(selectedAmenities = updated)
        }
        applyUiOnlyFilters()
    }

    private fun applyUiOnlyFilters() {
        _uiState.update { state ->
            val q = state.query.trim().lowercase()

            val filtered = state.houses.filter { house ->
                val matchesQuery = q.isBlank() ||
                        house.name.lowercase().contains(q) ||
                        house.neighborhood.lowercase().contains(q) ||
                        house.propertyType.lowercase().contains(q)

                val matchesBudget = when (state.selectedBudget) {
                    null -> true
                    "0-700" -> house.pricePerMonth in 0..700
                    "700-1000" -> house.pricePerMonth in 700..1000
                    "1000-1400" -> house.pricePerMonth in 1000..1400
                    "1400+" -> house.pricePerMonth >= 1400
                    else -> true
                }

                val matchesRoomType = state.selectedRoomType == null ||
                        house.propertyType.contains(state.selectedRoomType, ignoreCase = true)

                matchesQuery && matchesBudget && matchesRoomType
            }

            state.copy(visibleHouses = filtered)
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
            "${distanceMeters} m"
        } else {
            String.format("%.1f km", distanceMeters / 1000.0)
        }
    }

    fun onNotificationIconClick() {
        _uiState.update { state ->
            state.copy(
                showNotificationsPanel = true,
                notifications = state.notifications.map { it.copy(isRead = true) }
            )
        }
    }

    fun onDismissNotificationsPanel() {
        _uiState.update { it.copy(showNotificationsPanel = false) }
    }

    fun onClearNotifications() {
        _uiState.update { it.copy(notifications = emptyList(), showNotificationsPanel = false) }
    }
}
