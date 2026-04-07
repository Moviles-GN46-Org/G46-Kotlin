package com.example.g46_kotlin.features.roomie.domain.model

data class Roomie(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String?,
    val sleepSchedule: SleepSchedule,
    val cleanlinessLevel: CleanlinessLevel,
    val noisePreference: NoisePreference,
    val smokes: Boolean,
    val hasPets: Boolean,
    val budgetMin: Int,
    val budgetMax: Int,
    val preferredArea: String,
    val bio: String,
    val isActive: Boolean,
    val createdAt: String
)