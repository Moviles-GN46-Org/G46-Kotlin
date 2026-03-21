package com.example.g46_kotlin.features.house.domain.repository
import com.example.g46_kotlin.features.house.domain.model.PropertyDetail

interface HouseRepository {
    suspend fun getProperties(): List<PropertyDetail>

    suspend fun getPropertyById(id: String): PropertyDetail
}