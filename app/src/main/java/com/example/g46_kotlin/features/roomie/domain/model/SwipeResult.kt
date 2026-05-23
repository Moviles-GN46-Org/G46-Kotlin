package com.example.g46_kotlin.features.roomie.domain.model

data class SwipeResult (
    val swiped: Boolean,
    val matched: Boolean,
    val matchId: String? = null,
    val chatId: String? = null
)