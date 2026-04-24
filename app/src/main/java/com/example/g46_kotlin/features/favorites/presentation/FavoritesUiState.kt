package com.example.g46_kotlin.features.favorites.presentation

import com.example.g46_kotlin.cards.HousingCardUi

data class FavoritesUiState(
    val favoriteIds: Set<String> = emptySet(),
    val favoriteCards: List<HousingCardUi> = emptyList(),
    val isLoading: Boolean = false
)