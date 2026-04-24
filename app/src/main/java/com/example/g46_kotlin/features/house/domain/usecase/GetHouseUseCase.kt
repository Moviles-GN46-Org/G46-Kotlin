package com.example.g46_kotlin.features.house.domain.usecase

import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.SearchPropertiesFilters
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository

class GetHouseUseCase(
    private val repository: HouseRepository
) {
    suspend operator fun invoke(
        filters: SearchPropertiesFilters = SearchPropertiesFilters()
    ): List<PropertyDetail> {
        return repository.getProperties(filters)
    }
}