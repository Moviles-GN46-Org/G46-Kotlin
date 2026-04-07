package com.example.g46_kotlin.features.roomie.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SwipeDto (
    val swipedUserId: String,
    val direction: String
)

@Serializable
data class SwipeResponseDto (
    val success: Boolean,
    val data: MatchInfoDto
)

@Serializable
data class MatchDto(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val chatId: String,
    val createdAt: String,
)

@Serializable
data class MatchInfoDto(
    val swiped: Boolean,
    val matched: Boolean,
    val match: MatchDto?
)