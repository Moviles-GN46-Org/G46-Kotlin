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
    val lastActionMessage: String? = null,
    val notifications: List<HouseInAppNotification> = emptyList(),
    val showNotificationsPanel: Boolean = false
)

data class HouseInAppNotification(
    val id: String,
    val title: String,
    val message: String,
    val createdAtMillis: Long,
    val isRead: Boolean = false
)
