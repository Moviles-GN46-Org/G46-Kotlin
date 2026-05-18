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
import com.example.g46_kotlin.features.house.presentation.screen.PropertyDetailScreen
import com.example.g46_kotlin.features.house.presentation.PropertyDetailViewModel
import com.example.g46_kotlin.features.house.presentation.screen.PropertyDetailNoInternetScreen

@Composable
fun PropertyDetailRoute(
    onBackClick: () -> Unit,
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

    if (!isConnected) {
        PropertyDetailNoInternetScreen(onBackClick = onBackClick)
        return
    }
    val detail = uiState.detail

    //TODO: Implementar mejores pantallas en caso de error/loading
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
                onToggleFavorite = viewModel::onToggleFavorite,
                onBackClick = onBackClick
            )
        }
    }
}
