package com.example.g46_kotlin.core.location

import kotlinx.coroutines.flow.Flow

data class AppLocation(
    val lat: Double,
    val lon: Double
)

interface CurrentLocationSource {
    suspend fun getCurrentLocationOrNull(): AppLocation?
    fun observeLocationUpdates(): Flow<AppLocation>
}