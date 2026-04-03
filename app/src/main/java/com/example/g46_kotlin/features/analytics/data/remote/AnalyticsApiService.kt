package com.example.g46_kotlin.features.analytics.data.remote

import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventRequestDto
import com.example.g46_kotlin.features.analytics.data.remote.dto.SearchEventResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AnalyticsApiService {
    @POST("analytics/search-events")
    suspend fun trackSearchEvent(@Body body: SearchEventRequestDto): SearchEventResponseDto
}