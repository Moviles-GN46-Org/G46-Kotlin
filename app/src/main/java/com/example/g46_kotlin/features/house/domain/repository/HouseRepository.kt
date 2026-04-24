package com.example.g46_kotlin.features.house.domain.repository

import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.SearchPropertiesFilters

interface HouseRepository {
    suspend fun getProperties(filters: SearchPropertiesFilters = SearchPropertiesFilters()): List<PropertyDetail>

    suspend fun getPropertyById(id: String): PropertyDetail
}