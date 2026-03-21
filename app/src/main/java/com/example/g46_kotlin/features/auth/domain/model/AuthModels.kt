package com.example.g46_kotlin.features.auth.domain.model

enum class UserRole {
    STUDENT,
    LANDLORD,
    ADMIN
}

enum class AuthProvider {
    EMAIL,
    GOOGLE,
    FACEBOOK
}

enum class VerificationStatus {
    UNVERIFIED,
    PENDING,
    VERIFIED,
    REJECTED
}

data class User(
    val id: String,
    val email: String,
    val passwordHash: String?,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val role: UserRole,
    val authProvider: AuthProvider = AuthProvider.EMAIL,
    val profilePictureUrl: String?,
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String,
    val studentVerification: StudentVerification?,
    val landlordVerification: LandlordVerification?,
    val roommateProfile: RoommateProfile?
)

data class StudentVerification(
    val id: String,
    val userId: String,
    val universityEmail: String,
    val verificationCode: String,
    val codeExpiresAt: String,
    val status: VerificationStatus = VerificationStatus.UNVERIFIED,
    val verifiedAt: String?,
    val createdAt: String
)

data class LandlordVerification(
    val id: String,
    val userId: String,
    val idDocumentUrl: String,
    val utilityBillUrl: String,
    val status: VerificationStatus = VerificationStatus.PENDING,
    val reviewedBy: String?,
    val rejectionReason: String?,
    val reviewedAt: String?,
    val createdAt: String
)

enum class SleepSchedule {
    EARLY_BIRD,
    NIGHT_OWL,
    FLEXIBLE
}

enum class CleanlinessLevel {
    VERY_TIDY,
    MODERATE,
    RELAXED
}

enum class NoisePreference {
    QUIET,
    MODERATE,
    LIVELY
}

data class RoommateProfile(
    val id: String,
    val userId: String,
    val sleepSchedule: SleepSchedule,
    val cleanlinessLevel: CleanlinessLevel,
    val noisePreference: NoisePreference,
    val smokes: Boolean = false,
    val hasPets: Boolean = false,
    val budgetMin: Double,
    val budgetMax: Double,
    val preferredArea: String?,
    val bio: String?,
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String
)

data class LoginParams(
    val email: String,
    val password: String,
    val rememberMe: Boolean
)

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}