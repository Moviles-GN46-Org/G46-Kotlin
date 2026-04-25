package com.example.g46_kotlin.features.house.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.house.presentation.HouseViewModel
import com.example.g46_kotlin.features.house.presentation.screen.HouseScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HouseRoute(
    onMapClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: HouseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var isNavigating by remember { mutableStateOf(false) }
    HouseScreen(
        uiState = uiState,
        onNeighborhoodChange = viewModel::onNeighborhoodChange,
        onBudgetClick = viewModel::onBudgetClick,
        onMinBedroomsClick = viewModel::onMinBedroomsClick,
        onFurnishedToggle = viewModel::onFurnishedToggle,
        onPetFriendlyToggle = viewModel::onPetFriendlyToggle,
        onSortByDistanceToggle = viewModel::onSortByDistanceToggle,
        onRadiusKmChange = viewModel::onRadiusKmChange,
        onSearchSubmitted = viewModel::onSearchSubmitted,
        onApplyDistanceRecommendation = viewModel::onApplyDistanceRecommendation,
        onAvailabilityClick = viewModel::onAvailabilityClick,
        onMapClick = onMapClick,
        onPropertyClick = { id ->
            if (!isNavigating) {
                isNavigating = true
                onPropertyClick(id)
                scope.launch {
                    delay(2000)
                    isNavigating = false
                }
            }
        },
        onNotificationsClick = onNotificationsClick,
        onFavoritesClick = onFavoritesClick,
        onToggleFavorite = viewModel::onToggleFavorite,
        onNextPage = viewModel::onNextPage,
        onPrevPage = viewModel::onPrevPage
    )
}
