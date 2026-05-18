package com.example.g46_kotlin.features.favorites.domain.usecase

import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(
    private val repository: FavoritesRepository
) {
    suspend operator fun invoke(card: HousingCardUi) = repository.toggleFavorite(card)
}