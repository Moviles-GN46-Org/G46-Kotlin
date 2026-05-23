package com.example.g46_kotlin.features.roomie.data.mapper

import com.example.g46_kotlin.features.roomie.data.remote.dto.RoomieDto
import com.example.g46_kotlin.features.roomie.data.remote.dto.RoomieResponseDto
import com.example.g46_kotlin.features.roomie.domain.model.CleanlinessLevel
import com.example.g46_kotlin.features.roomie.domain.model.NoisePreference
import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.domain.model.SleepSchedule
import javax.inject.Inject

class RoomieMapper @Inject constructor() {
    fun toDomain(dto: RoomieDto): Roomie {
        return Roomie(
            id = dto.id,
            userId = dto.userId,
            firstName = dto.user.firstName,
            lastName = dto.user.lastName,
            profilePictureUrl = dto.user.profilePictureUrl,
            sleepSchedule = SleepSchedule.fromValue(dto.sleepSchedule) ?: SleepSchedule.FLEXIBLE,
            cleanlinessLevel = CleanlinessLevel.fromValue(dto.cleanlinessLevel)
                ?: CleanlinessLevel.MODERATE,
            noisePreference = NoisePreference.fromValue(dto.noisePreference)
                ?: NoisePreference.MODERATE,
            smokes = dto.smokes,
            hasPets = dto.hasPets,
            budgetMin = dto.budgetMin,
            budgetMax = dto.budgetMax,
            preferredArea = dto.preferredArea,
            bio = dto.bio,
            isActive = dto.isActive,
            createdAt = dto.createdAt,
            age = dto.age,
            matchRate = dto.matchRate,
            job = dto.job,
            university = dto.university
        )
    }
}