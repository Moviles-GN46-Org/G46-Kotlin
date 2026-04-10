package com.example.g46_kotlin.features.auth.data.remote.dto

import com.example.g46_kotlin.features.auth.domain.model.AuthProvider
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponseDto(
    val success: Boolean,
    val data: LoginDataDto
)

@Serializable
data class LoginDataDto(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val phone: String? = null,
    val profilePictureUrl: String? = null,
    val isVerified: Boolean,
    val authProvider: String,
    val isActive: Boolean,
    val createdAt: String

)
