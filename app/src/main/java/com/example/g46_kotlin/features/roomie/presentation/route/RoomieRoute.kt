package com.example.g46_kotlin.features.roomie.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.roomie.presentation.RoomieEffect
import com.example.g46_kotlin.features.roomie.presentation.RoomieUiEvent
import com.example.g46_kotlin.features.roomie.presentation.RoomieViewModel
import com.example.g46_kotlin.features.roomie.presentation.screen.RoomieOfflineScreen
import com.example.g46_kotlin.features.roomie.presentation.screen.RoomieScreen

@Composable
fun RoomieRoute(
    viewModel: RoomieViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit,
    onRoomieClick: (String) -> Unit,
    onNotifClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(RoomieUiEvent.OnScreenStarted)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RoomieEffect.ShowMessage -> {
                    // TODO implementar toast o algo para mostrar
                }
                is RoomieEffect.NavigateToRoomieDetail -> {
                    onRoomieClick(effect.roomieId)
                }
                is RoomieEffect.NavigateToMatches -> {
                    onChatClick(effect.chatId)
                }
            }
        }
    }

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    if (!isConnected) {
        RoomieOfflineScreen(
            onNotifClick = onBackClick
        )
    } else {
        RoomieScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onNotifClick = onNotifClick
        )
    }
}