package com.example.g46_kotlin.features.house.presentation

data class PropertyDetailUiState (
    val isLoading: Boolean = false,
    val detail: PropertyDetailUi? = null,
    val errorMessage: String? = null,
)