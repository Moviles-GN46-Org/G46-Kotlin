package com.example.g46_kotlin.features.auth.data.repository

import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import com.example.g46_kotlin.features.auth.data.remote.AuthApiService
import com.example.g46_kotlin.features.auth.data.remote.dto.LoginRequestDto
import com.example.g46_kotlin.features.auth.data.remote.dto.RegisterRequestDto
import com.example.g46_kotlin.features.auth.data.remote.dto.UserDto
import com.example.g46_kotlin.features.auth.domain.model.AuthProvider
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.AuthSession
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.model.User
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository
import com.example.g46_kotlin.features.auth.domain.repository.RegisterParams
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun loginWithEmail(params: LoginParams): AuthResult {
        return try {
            val response = api.login(
                body = LoginRequestDto(
                    email = params.email,
                    password = params.password
                )
            )

            val data = response.data
            tokenStorage.saveSession(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                persistent = params.rememberMe
            )

            val user = data.user.toDomainUser()
            val session = AuthSession(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                expiresInSeconds = data.expiresIn,
                user = user
            )
            AuthResult.Success(session)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string().orEmpty()
            when {
                e.code() == 409 -> AuthResult.Error("Email already in use")
                errorBody.contains("Account is not verified", ignoreCase = true) -> AuthResult.Error("Account is not verified")
                errorBody.contains("Email already in use", ignoreCase = true) -> AuthResult.Error("Email already in use")
                errorBody.contains("invalid", ignoreCase = true) -> AuthResult.Error("Invalid signup data")
                e.code() == 400 -> AuthResult.Error("Invalid signup data")
                else -> AuthResult.Error("Something went wrong (${e.code()})")
            }
        } catch (e: IOException) {
            AuthResult.Error("Network error, check your connection")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun register(params: RegisterParams): AuthResult {
        return try {
            val response = api.register(
                body = RegisterRequestDto(
                    email = params.email,
                    password = params.password,
                    firstName = params.firstName,
                    lastName = params.lastName,
                    role = params.role.name
                )
            )

            val data = response.data
            val session = AuthSession(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                expiresInSeconds = null,
                user = data.user.toDomainUser()
            )

            AuthResult.Success(session)
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> AuthResult.Error("Email already in use")
                400 -> AuthResult.Error("Invalid signup data")
                else -> AuthResult.Error("Something went wrong (${e.code()})")
            }
        } catch (e: IOException) {
            AuthResult.Error("Network error, check your connection")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Signup failed")
        }
    }

    private fun UserDto.toDomainUser(): User {
        return User(
            id = id,
            email = email,
            passwordHash = null,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            role = when (role.uppercase()) {
                "LANDLORD" -> UserRole.LANDLORD
                "ADMIN" -> UserRole.ADMIN
                else -> UserRole.STUDENT
            },
            authProvider = when (authProvider.uppercase()) {
                "GOOGLE" -> AuthProvider.GOOGLE
                "FACEBOOK" -> AuthProvider.FACEBOOK
                else -> AuthProvider.EMAIL
            },
            profilePictureUrl = profilePictureUrl,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = "",
            studentVerification = null,
            landlordVerification = null,
            roommateProfile = null
        )
    }
}