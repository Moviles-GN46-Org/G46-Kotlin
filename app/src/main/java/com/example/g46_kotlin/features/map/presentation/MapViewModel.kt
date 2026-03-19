package com.example.g46_kotlin.features.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.map.domain.usecase.GetNearbyApartmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getNearbyApartmentsUseCase: GetNearbyApartmentsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun onLocationResolved(lat: Double, lon: Double) {
        _uiState.update {
            it.copy(
                userLocation = UserLocationUI(
                    lat = lat,
                    lon = lon,
                ),
                errorMessage = null
            )
        }

        loadNearbyApartments(lat, lon)
    }

    fun retryLoadApartments(){
        val location = _uiState.value.userLocation ?: return
        loadNearbyApartments(location.lat, location.lon)
    }

    private fun loadNearbyApartments(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                getNearbyApartmentsUseCase(lat, lon)
            }.onSuccess { apartments ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        apartments = apartments.map { apt ->
                            ApartmentPinUi(
                                id = apt.id,
                                title = apt.title,
                                description = apt.description,
                                rating = apt.rating,
                                lat = apt.lat,
                                lon = apt.lon
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

}