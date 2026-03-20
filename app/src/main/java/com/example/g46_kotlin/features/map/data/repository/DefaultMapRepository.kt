package com.example.g46_kotlin.features.map.data.repository

import com.example.g46_kotlin.features.map.domain.repository.MapRepository

class DefaultMapRepository: MapRepository {
    override suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ) {

    }
}