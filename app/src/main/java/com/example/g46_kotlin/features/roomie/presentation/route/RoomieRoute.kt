package com.example.g46_kotlin.features.roomie.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.roomie.presentation.RoomieViewModel
import com.example.g46_kotlin.features.roomie.presentation.screen.RoomieScreen

@Composable
fun RoomieRoute(
    viewModel: RoomieViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit,
    onRoomieClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomieScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}