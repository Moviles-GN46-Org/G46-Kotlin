package com.example.g46_kotlin.features.chat.presentation.detail.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.chat.presentation.detail.ChatDetailViewModel
import com.example.g46_kotlin.features.chat.presentation.detail.screen.ChatDetailScreen

@Composable
fun ChatDetailRoute(
    onBackClick: () -> Unit,
    onViewListingClick: (String) -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.startPolling()
        onDispose { viewModel.stopPolling() }
    }

    ChatDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onInputChanged = viewModel::onInputChanged,
        onSendMessage = viewModel::onSendMessage,
        onViewListingClick = onViewListingClick
    )
}