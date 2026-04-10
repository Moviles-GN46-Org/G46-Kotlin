package com.example.g46_kotlin.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)

@Serializable
data class RegisterResponseDto(
    val success: Boolean,
    val data: RegisterDataDto
)

@Serializable
data class RegisterDataDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String?
)