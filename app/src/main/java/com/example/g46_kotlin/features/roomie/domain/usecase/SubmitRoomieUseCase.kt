package com.example.g46_kotlin.features.roomie.domain.usecase

import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.domain.model.SwipeResult
import com.example.g46_kotlin.features.roomie.domain.repository.RoomieRepository

class SubmitRoomieUseCase (
    private val repository: RoomieRepository
) {
    suspend operator fun invoke(roomieId: String, liked: Boolean): SwipeResult {
        return if (liked) {
            repository.swipeRoomie(roomieId = roomieId, direction = "RIGHT")
        } else {
            repository.swipeRoomie(roomieId = roomieId, direction = "LEFT")
        }
    }
}