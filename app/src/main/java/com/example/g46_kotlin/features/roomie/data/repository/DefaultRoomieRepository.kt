package com.example.g46_kotlin.features.roomie.data.repository

import com.example.g46_kotlin.features.roomie.data.mapper.RoomieMapper
import com.example.g46_kotlin.features.roomie.data.mapper.SwipeMapper
import com.example.g46_kotlin.features.roomie.data.remote.RoomieApiService
import com.example.g46_kotlin.features.roomie.data.remote.dto.SwipeDto
import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.domain.model.SwipeResult
import com.example.g46_kotlin.features.roomie.domain.repository.RoomieRepository
import javax.inject.Inject

class DefaultRoomieRepository @Inject constructor(
    private val roomieApiService: RoomieApiService,
    private val roomieMapper: RoomieMapper,
    private val swipeMapper: SwipeMapper
): RoomieRepository {
    override suspend fun getRecommendedRoomies(): List<Roomie> {
        val response = roomieApiService.getRoomies()
        return response.data.map { roomieMapper.toDomain(it) }
    }

    override suspend fun swipeRoomie(roomieId: String, direction: String): SwipeResult {
        val response = roomieApiService.swipeRoomie(SwipeDto(roomieId, direction))
        return swipeMapper.toDomain(response.data)
    }
}