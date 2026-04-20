package com.example.g46_kotlin.features.map.data.repository

import com.example.g46_kotlin.features.map.data.mapper.PropertyMapper
import com.example.g46_kotlin.features.map.data.remote.MapApiService
import com.example.g46_kotlin.features.map.domain.model.NearbyPropertiesResult
import com.example.g46_kotlin.features.map.domain.model.Property
import com.example.g46_kotlin.features.map.domain.repository.MapRepository
import javax.inject.Inject

class DefaultMapRepository @Inject constructor(
    private val mapApiService: MapApiService,
    private val propertyMapper: PropertyMapper
) : MapRepository {
    override suspend fun getNearbyApartments(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int
    ): NearbyPropertiesResult {
        val radiusKm = radiusMeters / 1000.0
        val response = mapApiService.getNearbyProperties(userLat, userLon, radiusKm)

        return propertyMapper.toNearbyPropertiesResult(response.data)
    }
}
