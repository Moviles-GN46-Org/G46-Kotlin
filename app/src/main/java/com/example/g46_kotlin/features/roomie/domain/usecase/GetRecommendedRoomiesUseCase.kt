package com.example.g46_kotlin.features.roomie.domain.usecase

import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.domain.repository.RoomieRepository

class GetRecommendedRoomiesUseCase (
    private val repository: RoomieRepository
) {
    suspend fun invoke(): List<Roomie> {
        return repository.getRecommendedRoomies()
    }
}