package com.example.g46_kotlin.features.house.presentation

data class PropertyDetailUiState (
    val isLoading: Boolean = false,
    val detail: PropertyDetailUi? = null,
    val errorMessage: String? = null,
    val isStartingChat: Boolean = false,
    val navigateToChatId: String? = null,
    val responseTimeBucket: String? = null
)