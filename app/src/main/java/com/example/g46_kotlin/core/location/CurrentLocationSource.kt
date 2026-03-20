package com.example.g46_kotlin.core.location

data class AppLocation(
    val lat: Double,
    val lon: Double
)

interface CurrentLocationSource {
    suspend fun getCurrentLocationOrNull(): AppLocation?
}