package com.example.g46_kotlin.features.roomie.presentation

sealed interface RoomieUiEvent {
    data object OnScreenStarted : RoomieUiEvent
    data object OnRefresh : RoomieUiEvent
    data object OnUndo: RoomieUiEvent
    data class OnLike(val roomieId: String) : RoomieUiEvent
    data class OnPass(val roomieId: String) : RoomieUiEvent
    data class OnCardClicked(val roomieId: String) : RoomieUiEvent
    data object OnRetryAfterError : RoomieUiEvent
}
