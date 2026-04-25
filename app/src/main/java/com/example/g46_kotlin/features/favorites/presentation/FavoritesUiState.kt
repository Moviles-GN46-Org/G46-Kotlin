package com.example.g46_kotlin.features.favorites.presentation

import com.example.g46_kotlin.cards.HousingCardUi

data class FavoritesUiState(
    val favoriteCards: List<HousingCardUi> = emptyList()
)