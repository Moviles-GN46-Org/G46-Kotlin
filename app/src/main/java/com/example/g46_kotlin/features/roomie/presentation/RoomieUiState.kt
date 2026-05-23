package com.example.g46_kotlin.features.roomie.presentation

import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCardUi

data class RoomieUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Cola de recomendaciones
    val queue: List<RoomieCardUi> = emptyList(),

    // Top card actual
    val current: RoomieCardUi? = null,

    // Progreso
    val totalLoaded: Int = 0,
    val seenCount: Int = 0,

    // UX swipe
    val isSubmittingDecision: Boolean = false,
    val canUndo: Boolean = false,

    // Fin de lista
    val endReached: Boolean = false
)
