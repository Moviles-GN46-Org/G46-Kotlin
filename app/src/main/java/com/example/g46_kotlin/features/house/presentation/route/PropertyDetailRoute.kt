package com.example.g46_kotlin.features.house.presentation.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.house.presentation.screen.PropertyDetailScreen
import com.example.g46_kotlin.features.house.presentation.PropertyDetailViewModel

@Composable
fun PropertyDetailRoute(
    onBackClick: () -> Unit,
    viewModel: PropertyDetailViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                onBackClick = onBackClick
            )
        }
    }
}
