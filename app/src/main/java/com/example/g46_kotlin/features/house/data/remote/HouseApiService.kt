package com.example.g46_kotlin.features.house.data.remote

import com.example.g46_kotlin.features.house.data.remote.dto.PropertiesDetailResponse
import com.example.g46_kotlin.features.house.data.remote.dto.PropertyDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HouseApiService {

    @GET("properties/")
    suspend fun getProperties(): PropertiesDetailResponse

    @GET(value = "properties/{id}")
    suspend fun getPropertyById(@Path("id") id: String): PropertyDetailResponse
}
