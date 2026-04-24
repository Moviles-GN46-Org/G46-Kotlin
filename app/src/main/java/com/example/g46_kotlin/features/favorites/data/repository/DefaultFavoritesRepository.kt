package com.example.g46_kotlin.features.favorites.data.repository

import com.example.g46_kotlin.features.favorites.data.local.SharedPrefsFavoritesSource
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultFavoritesRepository @Inject constructor(
    private val localSource: SharedPrefsFavoritesSource
) : FavoritesRepository {

    override fun getFavoriteIds(): Flow<Set<String>> = localSource.favoriteIds

    override suspend fun toggleFavorite(propertyId: String) = localSource.toggleFavorite(propertyId)

    override fun isFavorite(propertyId: String): Boolean = localSource.isFavorite(propertyId)
}