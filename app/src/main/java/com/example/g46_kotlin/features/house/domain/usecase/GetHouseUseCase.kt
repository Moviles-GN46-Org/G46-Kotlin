package com.example.g46_kotlin.features.house.domain.usecase

import com.example.g46_kotlin.features.house.domain.model.House
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository

class GetHouseUseCase(
    private val repository: HouseRepository
) {
    suspend operator fun invoke(): List<House> {
        return repository.getHomes()
    }
}