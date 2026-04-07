package com.example.g46_kotlin.features.roomie.domain.repository

import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.domain.model.SwipeResult

interface RoomieRepository {
    suspend fun getRecommendedRoomies(): List<Roomie>

    suspend fun swipeRoomie(roomieId: String, direction: String): SwipeResult
}