package com.example.g46_kotlin.features.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.core.location.CurrentLocationSource
import com.example.g46_kotlin.features.map.domain.usecase.GetNearbyApartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import com.example.g46_kotlin.features.analytics.data.repository.AnalyticsRepository



@HiltViewModel
class MapViewModel @Inject constructor(
    private val getNearbyApartmentsUseCase: GetNearbyApartmentsUseCase,
    private val currentLocationSource: CurrentLocationSource,
    private val analyticsRepository: AnalyticsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var locationTrackingJob: Job? = null
    private var lastApartmentsRequestLocation: UserLocationUI? = null

    private companion object {
        const val ANDES_LAT = 4.6016042953614225
        const val ANDES_LON = -74.06614174023011
        const val APARTMENTS_RELOAD_MIN_DISTANCE_METERS = 120.0
        const val DEFAULT_RADIUS_KM = 7.0
    }

    fun onApartmentSelected(id: String) {
        _uiState.update { it.copy(selectedApartmentId = id) }
    }

    fun startLocationTracking(hasLocationPermission: Boolean) {
        if (locationTrackingJob?.isActive == true) return

        if (!hasLocationPermission) {
            setFallbackIfNeeded()
            return
        }

        locationTrackingJob = viewModelScope.launch {
            val first = currentLocationSource.getCurrentLocationOrNull()
            if (first != null) {
                onLocationResolved(first.lat, first.lon)
            } else {
                setFallbackIfNeeded()
            }

            currentLocationSource.observeLocationUpdates().collect { loc ->
                onLocationResolved(loc.lat, loc.lon)
            }
        }

    }

    fun stopLocationTracking() {
        locationTrackingJob?.cancel()
        locationTrackingJob = null
    }

    fun retryLoadApartments() {
        val location = _uiState.value.userLocation ?: return
        loadNearbyApartments(location.lat, location.lon)
    }

    fun onLocationResolved(lat: Double, lon: Double) {
        val newLocation = UserLocationUI(lat, lon)

        _uiState.update {
            it.copy(
                userLocation = newLocation,
                errorMessage = null
            )
        }

        val shouldReload = shouldReloadApartments(newLocation)
        if (shouldReload) {
            loadNearbyApartments(lat, lon)
            lastApartmentsRequestLocation = newLocation
        }
    }

    private fun setFallbackIfNeeded() {
        if (_uiState.value.userLocation == null) {
            onLocationResolved(ANDES_LAT, ANDES_LON)
        }
    }

    private fun shouldReloadApartments(newLocation: UserLocationUI): Boolean {
        val last = lastApartmentsRequestLocation ?: return true
        val distance = distanceMeters(
            lat1 = last.lat,
            lon1 = last.lon,
            lat2 = newLocation.lat,
            lon2 = newLocation.lon
        )
        return distance >= APARTMENTS_RELOAD_MIN_DISTANCE_METERS
    }


    private fun loadNearbyApartments(lat: Double, lon: Double, force: Boolean = false) {
        if (!force && _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            launch {
                runCatching {
                    analyticsRepository.trackMapSearch(
                        lat = lat,
                        lng = lon,
                        radiusKm = DEFAULT_RADIUS_KM
                    )
                }
            }

            runCatching {
                getNearbyApartmentsUseCase(lat, lon)
            }.onSuccess { apartments ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        apartments = apartments.map { apt ->
                            PropertyPinUi(
                                id = apt.id,
                                title = shortenTitleForMap(apt.title, maxWords = 3),
                                description = apt.description,
                                rating = apt.rating,
                                lat = apt.lat,
                                lon = apt.lon,
                                price = formatCopToThousandsLabel(apt.price),
                                imageUrl = apt.image
                            )
                        }
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        apartments = emptyList(),
                        errorMessage = e.message ?: "Error cargando apartamentos cercanos"
                    )
                }
            }
        }
    }

    private fun distanceMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadius = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    override fun onCleared() {
        stopLocationTracking()
        super.onCleared()
    }
}