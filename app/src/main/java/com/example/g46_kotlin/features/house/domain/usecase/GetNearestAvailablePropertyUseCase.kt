package com.example.g46_kotlin.features.house.domain.usecase

import com.example.g46_kotlin.core.location.AppLocation
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.PropertyStatus
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class NearestPropertyResult(
    val property: PropertyDetail,
    val distanceMeters: Int
)

class GetNearestAvailablePropertyUseCase @Inject constructor() {
    operator fun invoke(
        userLocation: AppLocation,
        properties: List<PropertyDetail>
    ): NearestPropertyResult? {
        val activeProperties = properties.filter { it.status == PropertyStatus.ACTIVE }
        if (activeProperties.isEmpty()) return null

        val nearest = activeProperties.minByOrNull { property ->
            distanceMeters(
                lat1 = userLocation.lat,
                lon1 = userLocation.lon,
                lat2 = property.latitude,
                lon2 = property.longitude
            )
        } ?: return null

        val meters = distanceMeters(
            lat1 = userLocation.lat,
            lon1 = userLocation.lon,
            lat2 = nearest.latitude,
            lon2 = nearest.longitude
        )

        return NearestPropertyResult(
            property = nearest,
            distanceMeters = meters.toInt()
        )
    }

    private fun distanceMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusMeters = 6371000.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusMeters * c
    }
}

