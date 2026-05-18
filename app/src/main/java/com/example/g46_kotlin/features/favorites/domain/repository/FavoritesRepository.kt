package com.example.g46_kotlin.features.favorites.domain.repository

import com.example.g46_kotlin.cards.HousingCardUi
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteCards(): Flow<List<HousingCardUi>>
    fun getFavoriteIds(): Flow<Set<String>>
    suspend fun toggleFavorite(card: HousingCardUi)
    fun isFavorite(propertyId: String): Boolean
}