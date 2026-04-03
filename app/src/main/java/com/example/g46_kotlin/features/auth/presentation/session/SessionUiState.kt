package com.example.g46_kotlin.features.auth.presentation.session

sealed interface SessionUiState {
    data object Loading : SessionUiState
    data object Authenticated : SessionUiState
    data object Unauthenticated : SessionUiState
}