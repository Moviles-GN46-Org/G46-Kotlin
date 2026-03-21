package com.example.g46_kotlin.features.auth.domain.repository

import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.LoginParams

interface AuthRepository {
    suspend fun loginWithEmail(params: LoginParams): AuthResult
}
