package com.example.g46_kotlin.features.analytics.data.remote

import com.example.g46_kotlin.features.analytics.data.remote.dto.PreferredMaxDistanceSummaryResponseDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventRequestDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AnalyticsApiService {
    @POST("analytics/search-events")
    suspend fun trackSearchEvent(@Body body: SearchEventRequestDto): SearchEventResponseDto
    @GET("analytics/preferred-max-distance-summary")
    suspend fun getPreferredMaxDistanceSummary(
        @Query("from") fromIso: String,
        @Query("to") toIso: String
    ): PreferredMaxDistanceSummaryResponseDto

    @GET("analytics/my-preferred-max-distance")
    suspend fun getMyPreferredMaxDistance(
        @Query("from") fromIso: String,
        @Query("to") toIso: String
    ): PreferredMaxDistanceSummaryResponseDto
}