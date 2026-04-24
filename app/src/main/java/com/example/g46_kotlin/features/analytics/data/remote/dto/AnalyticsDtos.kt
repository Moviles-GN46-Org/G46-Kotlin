package com.example.g46_kotlin.features.analytics.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchEventRequestDto(
    val userId: String? = null,
    val sessionId: String,
    val query: String? = null,
    val city: String,
    val neighborhood: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val radiusKm: Double? = null,
    val filters: SearchFiltersDto = SearchFiltersDto(),
    val source: String,
    val searchedAt: String? = null
)

@Serializable
data class SearchFiltersDto(
    val budget: BudgetDto? = null,
    val propertyType: String? = null,
    val amenities: List<String> = emptyList(),
    val maxPrice: Int? = null,
    val minBedrooms: Int? = null,
    val furnished: Boolean? = null,
    val petFriendly: Boolean? = null,
    val sortBy: String? = null
)

@Serializable
data class BudgetDto(
    val min: Int? = null,
    val max: Int? = null
)

@Serializable
data class SearchEventResponseDto(
    val success: Boolean,
    val data: SearchEventDataDto
)

@Serializable
data class SearchEventDataDto(
    val id: String,
    val deduped: Boolean
)

@Serializable
data class PreferredMaxDistanceSummaryResponseDto(
    val success: Boolean,
    val data: PreferredMaxDistanceSummaryDataDto? = null
)

@Serializable
data class PreferredMaxDistanceSummaryDataDto(
    val preferredKm: Double? = null,
    val recommendedMaxKm: Double? = null,
    val maxObservedKm: Double? = null,
    val samples: Int = 0,
    val lastUpdatedAt: String? = null,
    val message: String = ""
)