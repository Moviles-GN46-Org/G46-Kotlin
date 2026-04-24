package com.example.g46_kotlin.features.favorites.di

import com.example.g46_kotlin.features.favorites.data.repository.DefaultFavoritesRepository
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import com.example.g46_kotlin.features.favorites.domain.usecase.GetFavoriteIdsUseCase
import com.example.g46_kotlin.features.favorites.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesModule {

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        defaultFavoritesRepository: DefaultFavoritesRepository
    ): FavoritesRepository = defaultFavoritesRepository

    @Provides
    @Singleton
    fun provideGetFavoriteIdsUseCase(
        repository: FavoritesRepository
    ): GetFavoriteIdsUseCase = GetFavoriteIdsUseCase(repository)

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(
        repository: FavoritesRepository
    ): ToggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
}