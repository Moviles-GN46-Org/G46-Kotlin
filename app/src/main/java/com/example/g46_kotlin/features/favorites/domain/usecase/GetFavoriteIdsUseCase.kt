package com.example.g46_kotlin.features.favorites.domain.usecase

import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteIdsUseCase(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<Set<String>> = repository.getFavoriteIds()
}