package com.example.g46_kotlin.features.house.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.house.domain.model.Property
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
            }.onSuccess { properties ->
                val mapped = properties.map { property -> property.toHousingCardUi() }

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

    private fun Property.toHousingCardUi(): HousingCardUi {
        val typeLabel = propertyType.name.replace("_", " ")
        val roomLabel = "$bedrooms Bed · $bathrooms Bath"

        return HousingCardUi(
            name = title,
            pricePerMonth = monthlyRent.toInt(),
            rating = 0.0,
            neighborhood = neighborhood,
            propertyType = "$typeLabel · $roomLabel",
        )
    }
}
