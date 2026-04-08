package com.example.g46_kotlin.features.notifications.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.notifications.presentation.NotificationsViewModel
import com.example.g46_kotlin.features.notifications.presentation.screen.NotificationsScreen

@Composable
fun NotificationsRoute(
    onBackClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onRoomieClick: (String) -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}