package com.example.g46_kotlin.features.map.data.repository

import com.example.g46_kotlin.features.map.domain.model.Apartment
import com.example.g46_kotlin.features.map.domain.repository.MapRepository
import kotlinx.coroutines.delay

class FakeMapRepository: MapRepository {
    override suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ): List<Apartment> {
        delay(500)
        return listOf(
            Apartment(
                "a1",
                "Apto Central",
                "2 habitaciones",
                4.6,
                userLat + 0.0020,
                userLon + 0.0015,
                "$1000"
            ),
            Apartment(
                "a2",
                "Studio Norte",
                "1 habitacion",
                4.2,
                userLat - 0.0012,
                userLon + 0.0022,
                "$1500"
            ),
            Apartment(
                "a3",
                "Loft Universitario",
                "Cerca al campus",
                4.8,
                userLat + 0.0010,
                userLon - 0.0018,
                "$2000"
            )
        )
    }
}