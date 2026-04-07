package com.example.g46_kotlin.features.roomie.data.remote.dto
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomieDto(
    val id: String,
    val userId: String,
    val user: RoomieUserDto,
    val sleepSchedule: String,
    val cleanlinessLevel: String,
    val noisePreference: String,
    val smokes: Boolean,
    val hasPets: Boolean,
    val budgetMin: Int,
    val budgetMax: Int,
    val preferredArea: String,
    val bio: String,
    val isActive: Boolean,
    val createdAt: String
)

@Serializable
data class RoomieUserDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String? = null
)

@Serializable
data class RoomieResponseDto (
    val success: Boolean,
    val data: List<RoomieDto>
)