package com.example.g46_kotlin.features.house.domain.model

enum class PropertySortBy(val apiValue: String) {
    PRICE("price"),
    DISTANCE("distance")
}

data class SearchPropertiesFilters(
    val maxPrice: Int? = null,
    val minBedrooms: Int? = null,
    val furnished: Boolean? = null,
    val petFriendly: Boolean? = null,
    val neighborhood: String? = null,
    val status: PropertyStatus? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val radiusKm: Double? = null,
    val sortBy: PropertySortBy? = null,
    val page: Int? = 1,
    val limit: Int? = 20,
    val includeAveragePrice: Boolean? = null
)