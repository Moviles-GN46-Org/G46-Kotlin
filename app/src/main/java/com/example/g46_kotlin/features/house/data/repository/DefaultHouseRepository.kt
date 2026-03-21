package com.example.g46_kotlin.features.house.data.repository

import com.example.g46_kotlin.features.house.data.mapper.PropertyDetailMapper
import com.example.g46_kotlin.features.house.data.remote.HouseApiService
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import javax.inject.Inject

class DefaultHouseRepository @Inject constructor(
    private val houseApiService: HouseApiService,
    private val propertyMapper: PropertyDetailMapper
): HouseRepository{
    override suspend fun getProperties(): List<PropertyDetail> {
        val response = houseApiService.getProperties()
        return response.data.properties.map { propertyMapper.toDomain(it) }
    }

    override suspend fun getPropertyById(id: String): PropertyDetail {
        val response = houseApiService.getPropertyById(id)
        return propertyMapper.toDomain(response.data)
    }
}