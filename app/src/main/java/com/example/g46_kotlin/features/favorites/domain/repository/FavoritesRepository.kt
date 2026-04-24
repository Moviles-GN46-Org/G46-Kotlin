package com.example.g46_kotlin.features.favorites.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteIds(): Flow<Set<String>>
    suspend fun toggleFavorite(propertyId: String)
    fun isFavorite(propertyId: String): Boolean
}