package com.example.g46_kotlin.features.house.domain.usecase

import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository

class GetPropertyByIdUseCase (
    private val repository: HouseRepository
) {
    suspend operator fun invoke(propertyId: String): PropertyDetail {
        return repository.getPropertyById(propertyId)
    }
}