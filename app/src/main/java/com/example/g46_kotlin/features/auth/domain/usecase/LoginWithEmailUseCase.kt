package com.example.g46_kotlin.features.auth.domain.usecase

import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository

class LoginWithEmailUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(params: LoginParams): AuthResult {
        val email = params.email.trim()

        if (email.isBlank()) {
            return AuthResult.Error("Email is required")
        }

        if (!isValidEmail(email)) {
            return AuthResult.Error("Enter a valid email")
        }

        if (params.password.isBlank()) {
            return AuthResult.Error("Password is required")
        }

        if (params.password.length < 6) {
            return AuthResult.Error("Password must have at least 6 characters")
        }

        return repository.loginWithEmail(params.copy(email = email))
    }

    private fun isValidEmail(value: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(value)
    }
}