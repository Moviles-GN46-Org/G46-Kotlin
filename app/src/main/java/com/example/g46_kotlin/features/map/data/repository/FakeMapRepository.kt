package com.example.g46_kotlin.features.map.data.repository

import com.example.g46_kotlin.features.map.domain.model.NearbyPropertiesResult
import com.example.g46_kotlin.features.map.domain.model.PopularSize
import com.example.g46_kotlin.features.map.domain.model.Property
import com.example.g46_kotlin.features.map.domain.repository.MapRepository
import kotlinx.coroutines.delay

class FakeMapRepository : MapRepository {
    override suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ): NearbyPropertiesResult {
        delay(500)

        val properties = listOf(
            Property(
                id = "a1",
                title = "Apto Central",
                description = "2 habitaciones",
                rating = 4.6,
                lat = userLat + 0.0020,
                lon = userLon + 0.0015,
                price = "$1'000",
                image = ""
            ),
            Property(
                id = "a2",
                title = "Studio Norte",
                description = "1 habitación",
                rating = 4.2,
                lat = userLat - 0.0012,
                lon = userLon + 0.0022,
                price = "$1'500",
                image = ""
            ),
            Property(
                id = "a3",
                title = "Loft Universitario",
                description = "Cerca al campus",
                rating = 4.8,
                lat = userLat + 0.0010,
                lon = userLon - 0.0018,
                price = "$2'000",
                image = ""
            )
        )

        return NearbyPropertiesResult(
            properties = properties,
            avgRent = 1500.0
        )
    }

    override suspend fun getTopPropertySize(): PopularSize {
        delay(500)
        return PopularSize(
            minM2 = 20,
            maxM2 = 30,
            count = 10,
            avgSizeM2 = 25.0
        )
    }
}