package com.example.g46_kotlin.features.auth.data.repository

import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import com.example.g46_kotlin.features.auth.data.remote.AuthApiService
import com.example.g46_kotlin.features.auth.data.remote.dto.LoginRequestDto
import com.example.g46_kotlin.features.auth.data.remote.dto.UserDto
import com.example.g46_kotlin.features.auth.domain.model.AuthProvider
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.AuthSession
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.model.User
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun loginWithEmail(params: LoginParams): AuthResult {
        return runCatching {
            val response = api.login(
                body = LoginRequestDto(
                    email = params.email,
                    password = params.password
                )
            )

            val data = response.data
            tokenStorage.saveAccessToken(data.accessToken)
            tokenStorage.saveRefreshToken(data.refreshToken)

            val user = data.user.toDomainUser()
            val session = AuthSession(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                expiresInSeconds = data.expiresIn,
                user = user
            )
            AuthResult.Success(session)
        }.getOrElse { e ->
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    private fun UserDto.toDomainUser(): User {
        return User(
            id = id,
            email = email,
            passwordHash = null,
            firstName = firstName,
            lastName = lastName,
            phone = null,
            role = when (role.uppercase()) {
                "LANDLORD" -> UserRole.LANDLORD
                "ADMIN" -> UserRole.ADMIN
                else -> UserRole.STUDENT
            },
            authProvider = AuthProvider.EMAIL,
            profilePictureUrl = null,
            isActive = true,
            createdAt = "",
            updatedAt = "",
            studentVerification = null,
            landlordVerification = null,
            roommateProfile = null
        )
    }
}
