package com.example.g46_kotlin.features.house.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.house.presentation.HouseViewModel
import com.example.g46_kotlin.features.house.presentation.screen.HouseScreen

@Composable
fun HouseRoute(
    onMapClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    viewModel: HouseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HouseScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onBudgetClick = viewModel::onBudgetClick,
        onRoomTypeClick = viewModel::onRoomTypeClick,
        onAmenityClick = viewModel::onAmenityClick,
        onAvailabilityClick = viewModel::onAvailabilityClick,
        onMapClick = onMapClick,
        onPropertyClick = onPropertyClick,
        onNotificationsClick = onNotificationsClick
    )
}
