package com.example.g46_kotlin.features.analytics.data.repository

import com.example.g46_kotlin.features.analytics.data.remote.AnalyticsApiService
import com.example.g46_kotlin.features.analytics.data.remote.AppSessionIdProvider
import com.example.g46_kotlin.features.analytics.data.remote.dto.PreferredMaxDistanceSummaryDataDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventRequestDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchFiltersDto
import com.example.g46_kotlin.features.house.domain.model.PropertySortBy
import com.example.g46_kotlin.features.house.domain.model.SearchPropertiesFilters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val api: AnalyticsApiService,
    private val sessionProvider: AppSessionIdProvider
) {
    suspend fun trackSearchSubmitted(
        filters: SearchPropertiesFilters,
        neighborhoodQuery: String?
    ) {
        api.trackSearchEvent(
            SearchEventRequestDto(
                userId = null,
                sessionId = sessionProvider.sessionId,
                query = neighborhoodQuery?.trim()?.takeIf { it.isNotEmpty() },
                city = "Bogota",
                neighborhood = filters.neighborhood,
                lat = filters.lat,
                lng = filters.lng,
                radiusKm = filters.radiusKm,
                source = "house_list",
                filters = SearchFiltersDto(
                    maxPrice = filters.maxPrice,
                    minBedrooms = filters.minBedrooms,
                    furnished = filters.furnished,
                    petFriendly = filters.petFriendly,
                    sortBy = when (filters.sortBy) {
                        PropertySortBy.DISTANCE -> "distance"
                        PropertySortBy.PRICE -> "price"
                        null -> null
                    }
                )
            )
        )
    }

    suspend fun trackDistanceFilterChanged(
        radiusKm: Double?,
        lat: Double?,
        lng: Double?,
        neighborhood: String?
    ) {
        api.trackSearchEvent(
            SearchEventRequestDto(
                userId = null,
                sessionId = sessionProvider.sessionId,
                city = "Bogota",
                neighborhood = neighborhood,
                lat = lat,
                lng = lng,
                radiusKm = radiusKm,
                source = "house_list"
            )
        )
    }

    suspend fun trackDistanceRecommendationApplied(
        recommendedRadiusKm: Double,
        lat: Double?,
        lng: Double?,
        neighborhood: String?
    ) {
        api.trackSearchEvent(
            SearchEventRequestDto(
                userId = null,
                sessionId = sessionProvider.sessionId,
                query = "DISTANCE_RECOMMENDATION_APPLIED",
                city = "Bogota",
                neighborhood = neighborhood,
                lat = lat,
                lng = lng,
                radiusKm = recommendedRadiusKm,
                source = "house_list"
            )
        )
    }

    suspend fun getPreferredMaxDistanceSummary(
        fromIso: String,
        toIso: String
    ): PreferredMaxDistanceSummaryDataDto? {
        return api.getPreferredMaxDistanceSummary(fromIso = fromIso, toIso = toIso).data
    }

    suspend fun getMyPreferredMaxDistance(
        fromIso: String,
        toIso: String
    ): PreferredMaxDistanceSummaryDataDto? {
        return api.getMyPreferredMaxDistance(fromIso = fromIso, toIso = toIso).data
    }

    suspend fun trackMapSearch(lat: Double, lng: Double, radiusKm: Double) {
        api.trackSearchEvent(
            SearchEventRequestDto(
                userId = null,
                sessionId = sessionProvider.sessionId,
                city = "Bogota",
                lat = lat,
                lng = lng,
                radiusKm = radiusKm,
                source = "map"
            )
        )
    }
}
