package com.example.g46_kotlin.features.auth.domain.repository

import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.model.UserRole

data class RegisterParams(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole
)

interface AuthRepository {
    suspend fun loginWithEmail(params: LoginParams): AuthResult
    suspend fun register(params: RegisterParams): AuthResult
}