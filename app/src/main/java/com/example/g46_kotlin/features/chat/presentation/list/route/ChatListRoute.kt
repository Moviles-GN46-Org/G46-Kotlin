package com.example.g46_kotlin.features.chat.presentation.list.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.chat.presentation.list.ChatListViewModel
import com.example.g46_kotlin.features.chat.presentation.list.screen.ChatListScreen

@Composable
fun ChatListRoute(
    onChatClick: (String) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.startPolling()
        onDispose { viewModel.stopPolling() }
    }

    ChatListScreen(
        uiState = uiState,
        onChatClick = onChatClick,
        onRetryClick = viewModel::onRetry
    )
}