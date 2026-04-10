package com.example.g46_kotlin.features.house.presentation
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail

data class HouseUiState(
    val isLoading: Boolean = false,
    val houses: List<HousingCardUi> = emptyList(),
    val visibleHouses: List<HousingCardUi> = emptyList(),
    val query: String = "",
    val selectedBudget: String? = null,
    val selectedRoomType: String? = null,
    val selectedAmenities: Set<String> = emptySet(),
    val errorMessage: String? = null,
    val lastActionMessage: String? = null,
    val allProperties: List<PropertyDetail> = emptyList()
)
