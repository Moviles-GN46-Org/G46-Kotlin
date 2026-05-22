package com.example.g46_kotlin.features.roomie.presentation

sealed interface RoomieEffect {
    data class ShowMessage(val text: String) : RoomieEffect
    data class NavigateToRoomieDetail(val roomieId: String) : RoomieEffect
    data class NavigateToMatches(val chatId: String) : RoomieEffect
}