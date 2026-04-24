package com.example.g46_kotlin.features.house.data.remote

import com.example.g46_kotlin.features.house.data.remote.dto.PropertiesDetailResponse
import com.example.g46_kotlin.features.house.data.remote.dto.PropertyDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HouseApiService {

    @GET("properties")
    suspend fun getProperties(
        @Query("maxPrice") maxPrice: Int? = null,
        @Query("minBedrooms") minBedrooms: Int? = null,
        @Query("furnished") furnished: Boolean? = null,
        @Query("petFriendly") petFriendly: Boolean? = null,
        @Query("neighborhood") neighborhood: String? = null,
        @Query("status") status: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null,
        @Query("radiusKm") radiusKm: Double? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("includeAveragePrice") includeAveragePrice: Boolean? = null
    ): PropertiesDetailResponse

    @GET("properties/{id}")
    suspend fun getPropertyById(@Path("id") id: String): PropertyDetailResponse
}