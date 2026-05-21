package com.example.g46_kotlin.features.house.presentation.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.house.presentation.PropertyDetailViewModel
import com.example.g46_kotlin.features.house.presentation.screen.PropertyDetailNoInternetScreen
import com.example.g46_kotlin.features.house.presentation.screen.PropertyDetailScreen

@Composable
fun PropertyDetailRoute(
    onBackClick: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    viewModel: PropertyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    LaunchedEffect(isConnected) {
        if (isConnected && uiState.detail == null && !uiState.isLoading) {
            viewModel.loadProperty()
        }
    }

    LaunchedEffect(uiState.navigateToChatId) {
        val chatId = uiState.navigateToChatId
        if (chatId != null) {
            viewModel.onChatNavigated()
            onNavigateToChat(chatId)
        }
    }

    if (!isConnected) {
        PropertyDetailNoInternetScreen(onBackClick = onBackClick)
        return
    }

    val detail = uiState.detail

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        detail != null -> {
            PropertyDetailScreen(
                detail = detail,
                isFavorite = isFavorite,
                isStartingChat = uiState.isStartingChat,
                onToggleFavorite = viewModel::onToggleFavorite,
                onBackClick = onBackClick,
                onContactClick = viewModel::onContactLandlord,
                responseTimeBucket = uiState.responseTimeBucket
            )
        }
    }
}
