package com.example.g46_kotlin.features.roomie.data.remote

import com.example.g46_kotlin.features.roomie.data.remote.dto.RoomieResponseDto
import com.example.g46_kotlin.features.roomie.data.remote.dto.SwipeDto
import com.example.g46_kotlin.features.roomie.data.remote.dto.SwipeResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoomieApiService {
    @GET("roommate/candidates")
    suspend fun getRoomies(): RoomieResponseDto

    @POST("roommate/swipe")
    suspend fun swipeRoomie(@Body body: SwipeDto): SwipeResponseDto
}