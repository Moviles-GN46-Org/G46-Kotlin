package com.example.g46_kotlin.features.map.data.remote

import com.example.g46_kotlin.features.map.data.remote.dto.ApartmentDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiService {
    @GET("apartments/nearby")
    suspend fun getNearbyApartments(
        @Query("lat") userLat: Double,
        @Query("lon") userLon: Double,
        @Query("radius") radiusMeters: Int
    ): List<ApartmentDto>
}