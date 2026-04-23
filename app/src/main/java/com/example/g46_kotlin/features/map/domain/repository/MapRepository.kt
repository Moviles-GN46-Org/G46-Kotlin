package com.example.g46_kotlin.features.map.domain.repository

import com.example.g46_kotlin.features.map.domain.model.NearbyPropertiesResult
import com.example.g46_kotlin.features.map.domain.model.PopularSize

interface MapRepository {
    suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ): NearbyPropertiesResult

    suspend fun getTopPropertySize(): PopularSize
}