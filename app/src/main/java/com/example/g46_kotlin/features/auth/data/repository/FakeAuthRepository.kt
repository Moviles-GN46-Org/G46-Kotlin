package com.example.g46_kotlin.features.auth.data.repository

import com.example.g46_kotlin.features.auth.domain.model.AuthProvider
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.AuthSession
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.model.StudentVerification
import com.example.g46_kotlin.features.auth.domain.model.User
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.features.auth.domain.model.VerificationStatus
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class FakeAuthRepository : AuthRepository {

    override suspend fun loginWithEmail(params: LoginParams): AuthResult {
        delay(700)

        val email = params.email.trim().lowercase()
        val password = params.password

        return if (email == "student@casandes.edu" && password == "123456") {
            val user = User(
                id = "u-student-1",
                email = email,
                passwordHash = null,
                firstName = "Demo",
                lastName = "Student",
                phone = null,
                role = UserRole.STUDENT,
                authProvider = AuthProvider.EMAIL,
                profilePictureUrl = null,
                isActive = true,
                createdAt = "2026-03-20T10:00:00Z",
                updatedAt = "2026-03-20T10:00:00Z",
                studentVerification = StudentVerification(
                    id = "sv-1",
                    userId = "u-student-1",
                    universityEmail = "student@casandes.edu",
                    verificationCode = "000000",
                    codeExpiresAt = "2026-12-31T23:59:59Z",
                    status = VerificationStatus.UNVERIFIED,
                    verifiedAt = null,
                    createdAt = "2026-03-20T10:00:00Z"
                ),
                landlordVerification = null,
                roommateProfile = null
            )
            AuthResult.Success(
                session = AuthSession(
                    accessToken = "fake-jwt-token",
                    refreshToken = "fake-refresh-token",
                    expiresInSeconds = 3600,
                    user = user
                )
            )
        } else {
            AuthResult.Error("Invalid email or password")
        }
    }
}
