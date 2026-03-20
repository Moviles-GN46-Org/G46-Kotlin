package com.example.g46_kotlin.features.house.domain.repository
import com.example.g46_kotlin.features.house.domain.model.House

interface HouseRepository {
    suspend fun getHomes(): List<House>
}