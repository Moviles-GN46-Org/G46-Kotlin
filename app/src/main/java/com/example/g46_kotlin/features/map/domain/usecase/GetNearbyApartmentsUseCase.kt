package com.example.g46_kotlin.features.map.domain.usecase

import com.example.g46_kotlin.features.map.domain.model.Apartment
import com.example.g46_kotlin.features.map.domain.repository.MapRepository

class GetNearbyApartmentsUseCase (
    private val repository: MapRepository
){
    suspend operator fun invoke(
        userLat: Double,
        userLon: Double,
        radiusMeters: Int = 3000
    ): List<Apartment> {
        return repository.getNearbyApartments(userLat, userLon, radiusMeters)
    }
}