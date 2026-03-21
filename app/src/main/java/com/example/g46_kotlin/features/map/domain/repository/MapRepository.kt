package com.example.g46_kotlin.features.map.domain.repository

import com.example.g46_kotlin.features.map.domain.model.Property

interface MapRepository {
    suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ): List<Property>
}