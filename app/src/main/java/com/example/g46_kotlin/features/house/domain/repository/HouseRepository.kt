package com.example.g46_kotlin.features.house.domain.repository
import com.example.g46_kotlin.features.house.domain.model.Property

interface HouseRepository {
    suspend fun getProperties(): List<Property>
}