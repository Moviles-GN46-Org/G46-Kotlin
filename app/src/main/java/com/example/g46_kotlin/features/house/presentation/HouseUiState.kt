package com.example.g46_kotlin.features.house.presentation
import com.example.g46_kotlin.cards.HousingCardUi

data class HouseUiState(
    val isLoading: Boolean = false,
    val houses: List<HousingCardUi> = emptyList(),
    val visibleHouses: List<HousingCardUi> = emptyList(),
    val query: String = "",
    val selectedBudget: String? = null,
    val selectedRoomType: String? = null,
    val selectedAmenities: Set<String> = emptySet(),
    val errorMessage: String? = null,
    val selectedHouseName: String? = null,
    val lastActionMessage: String? = null
)
