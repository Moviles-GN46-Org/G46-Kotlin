package com.example.g46_kotlin.features.analytics.data.repository

import com.example.g46_kotlin.features.analytics.data.remote.AppSessionIdProvider
import com.example.g46_kotlin.features.analytics.data.remote.AnalyticsApiService
import com.example.g46_kotlin.features.analytics.data.remote.dto.BudgetDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventRequestDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchFiltersDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val api: AnalyticsApiService,
    private val sessionProvider: AppSessionIdProvider
) {
    suspend fun trackHouseSearch(
        query: String,
        budget: String?,
        roomType: String?,
        amenities: Set<String>
    ) {
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) return
        api.trackSearchEvent(
            SearchEventRequestDto(
                userId = null,
                sessionId = sessionProvider.sessionId,
                query = cleanQuery,
                city = "Bogota",
                neighborhood = cleanQuery.takeIf { it.length >= 3 },
                source = "house_list",
                filters = SearchFiltersDto(
                    budget = parseBudget(budget),
                    propertyType = roomType,
                    amenities = amenities.toList()
                )
            )
        )
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

    private fun parseBudget(value: String?): BudgetDto? {
        return when (value) {
            "0-700" -> BudgetDto(min = 0, max = 700)
            "700-1000" -> BudgetDto(min = 700, max = 1000)
            "1000-1400" -> BudgetDto(min = 1000, max = 1400)
            "1400+" -> BudgetDto(min = 1400, max = null)
            else -> null
        }
    }
}
