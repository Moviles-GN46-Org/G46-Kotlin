package com.example.g46_kotlin.features.auth.data.remote

import com.example.g46_kotlin.features.auth.data.remote.dto.RefreshTokenRequestDto
import com.example.g46_kotlin.features.auth.data.remote.dto.RefreshTokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRefreshApiService {
    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: RefreshTokenRequestDto): RefreshTokenResponseDto
}
