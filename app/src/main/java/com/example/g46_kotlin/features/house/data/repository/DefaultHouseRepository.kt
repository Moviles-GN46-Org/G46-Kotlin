package com.example.g46_kotlin.features.house.data.repository

import com.example.g46_kotlin.features.house.data.mapper.PropertyDetailMapper
import com.example.g46_kotlin.features.house.data.remote.HouseApiService
import com.example.g46_kotlin.features.house.domain.model.PagedProperties
import com.example.g46_kotlin.features.house.domain.model.PropertySortBy
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.SearchPropertiesFilters
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import javax.inject.Inject

class DefaultHouseRepository @Inject constructor(
    private val houseApiService: HouseApiService,
    private val propertyMapper: PropertyDetailMapper
) : HouseRepository {

    override suspend fun getProperties(filters: SearchPropertiesFilters): PagedProperties {
        val sanitized = sanitize(filters)

        val response = houseApiService.getProperties(
            maxPrice = sanitized.maxPrice,
            minBedrooms = sanitized.minBedrooms,
            furnished = sanitized.furnished,
            petFriendly = sanitized.petFriendly,
            neighborhood = sanitized.neighborhood,
            status = sanitized.status?.name,
            lat = sanitized.lat,
            lng = sanitized.lng,
            radiusKm = sanitized.radiusKm,
            sortBy = sanitized.sortBy?.apiValue,
            page = sanitized.page,
            limit = sanitized.limit,
            includeAveragePrice = sanitized.includeAveragePrice
        )

        val totalPages = if (response.data.limit > 0)
            (response.data.total + response.data.limit - 1) / response.data.limit
        else 1

        return PagedProperties(
            properties = response.data.properties.map { propertyMapper.toDomain(it) },
            totalPages = totalPages,
            currentPage = response.data.page
        )
    }

    override suspend fun getPropertyById(id: String): PropertyDetail {
        val response = houseApiService.getPropertyById(id)
        return propertyMapper.toDomain(response.data)
    }

    private fun sanitize(filters: SearchPropertiesFilters): SearchPropertiesFilters {
        val hasLatLng = filters.lat != null && filters.lng != null
        val hasDistanceTriplet = hasLatLng && filters.radiusKm != null

        val normalizedSort = when {
            filters.sortBy == PropertySortBy.DISTANCE && hasLatLng -> PropertySortBy.DISTANCE
            filters.sortBy == PropertySortBy.PRICE -> PropertySortBy.PRICE
            else -> null
        }

        return filters.copy(
            neighborhood = filters.neighborhood?.trim()?.takeIf { it.isNotEmpty() },
            lat = if (hasLatLng) filters.lat else null,
            lng = if (hasLatLng) filters.lng else null,
            radiusKm = if (hasDistanceTriplet) filters.radiusKm else null,
            sortBy = normalizedSort,
            page = filters.page ?: 1
        )
    }
}