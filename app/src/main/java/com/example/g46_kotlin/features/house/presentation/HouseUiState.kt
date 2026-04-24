package com.example.g46_kotlin.features.house.presentation

import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail

data class GlobalDistanceInsightUi(
    val preferredKm: Double?,
    val recommendedMaxKm: Double?,
    val maxObservedKm: Double?,
    val samples: Int,
    val lastUpdatedAt: String?,
    val message: String
)

data class HouseUiState(
    val isLoading: Boolean = false,
    val houses: List<HousingCardUi> = emptyList(),
    val neighborhoodQuery: String = "",
    val selectedBudget: String? = null,
    val selectedMinBedrooms: Int? = null,
    val furnished: Boolean? = null,
    val petFriendly: Boolean? = null,
    val sortByDistance: Boolean = false,
    val radiusKmInput: String = "",
    val page: Int = 1,
    val limit: Int = 20,
    val errorMessage: String? = null,
    val lastActionMessage: String? = null,
    val allProperties: List<PropertyDetail> = emptyList(),
    val isGlobalDistanceInsightLoading: Boolean = false,
    val globalDistanceInsight: GlobalDistanceInsightUi? = null,
    val globalDistanceInsightFallbackMessage: String? = null,
    val favoriteIds: Set<String> = emptySet()
)