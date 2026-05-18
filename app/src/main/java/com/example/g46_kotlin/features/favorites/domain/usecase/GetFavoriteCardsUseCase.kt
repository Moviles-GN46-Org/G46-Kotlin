package com.example.g46_kotlin.features.favorites.domain.usecase

import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteCardsUseCase(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<List<HousingCardUi>> = repository.getFavoriteCards()
}