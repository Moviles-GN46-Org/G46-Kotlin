package com.example.g46_kotlin.features.map.domain.usecase

import com.example.g46_kotlin.features.map.domain.model.NearbyPropertiesResult
import com.example.g46_kotlin.features.map.domain.model.Property
import com.example.g46_kotlin.features.map.domain.repository.MapRepository

class GetNearbyApartmentsUseCase (
    private val repository: MapRepository
){
    suspend operator fun invoke(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int = 7000
    ): NearbyPropertiesResult {
        return repository.getNearbyApartments(userLat, userLon, radiusMeters)
    }
}