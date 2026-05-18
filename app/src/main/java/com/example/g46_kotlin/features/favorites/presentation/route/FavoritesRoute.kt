package com.example.g46_kotlin.features.favorites.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.favorites.presentation.FavoritesViewModel
import com.example.g46_kotlin.features.favorites.presentation.screen.FavoritesScreen

@Composable
fun FavoritesRoute(
    onBackClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPropertyClick = onPropertyClick,
        onToggleFavorite = viewModel::onToggleFavorite
    )
}