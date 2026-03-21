package com.example.g46_kotlin.features.map.data.remote

import com.example.g46_kotlin.features.map.data.remote.dto.PropertiesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiService {
    @GET("properties/")
    suspend fun getNearbyProperties(
        @Query("lat") userLat: Double,
        @Query("lng") userLon: Double,
        @Query("radiusKm") radiusKm: Double
    ): PropertiesResponse
}