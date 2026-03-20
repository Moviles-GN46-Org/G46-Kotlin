package com.example.g46_kotlin.features.house.presentation

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

@HiltViewModel
class HouseViewModel @Inject constructor(
    private val getHouseUseCase: GetHouseUseCase
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
            }.onSuccess { houses ->
                val mapped = houses.map { house ->
                    HousingCardUi(
                        name = house.title,
                        pricePerMonth = house.pricePerMonth,
                        rating = house.rating,
                        distanceToCampus = house.distanceToCampus,
                        propertyType = house.propertyType,
                        isVerified = house.isVerified,
                        isLiked = house.isLiked
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        houses = mapped,
                        visibleHouses = mapped
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        houses = emptyList(),
                        visibleHouses = emptyList(),
                        errorMessage = error.message ?: "Error cargando casas"
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

    // UI-only por ahora: mantiene todas las casas visibles.
    private fun applyUiOnlyFilters() {
        _uiState.update { it.copy(visibleHouses = it.houses) }
    }

    fun onHouseClick(houseName: String) {
        _uiState.update {
            it.copy(
                selectedHouseName = houseName,
                lastActionMessage = "Abrir detalle de $houseName"
            )
        }
    }

    fun onAvailabilityClick(houseName: String) {
        _uiState.update {
            it.copy(lastActionMessage = "Ver disponibilidad de $houseName")
        }
    }

    fun onLikeClick(houseName: String) {
        _uiState.update { state ->
            val updated = state.houses.map { house ->
                if (house.name == houseName) {
                    house.copy(isLiked = !house.isLiked)
                } else {
                    house
                }
            }

            val likedNow = updated.firstOrNull { it.name == houseName }?.isLiked == true

            state.copy(
                houses = updated,
                visibleHouses = updated,
                lastActionMessage = if (likedNow) "Te gusta $houseName" else "Quitaste like de $houseName"
            )
        }
    }
}
