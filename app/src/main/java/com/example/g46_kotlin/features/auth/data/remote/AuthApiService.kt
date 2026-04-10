package com.example.g46_kotlin.features.auth.data.remote

import com.example.g46_kotlin.features.auth.data.remote.dto.LoginRequestDto
import com.example.g46_kotlin.features.auth.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto
}
