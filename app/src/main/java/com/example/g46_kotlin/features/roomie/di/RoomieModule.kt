package com.example.g46_kotlin.features.roomie.di

import com.example.g46_kotlin.features.roomie.data.repository.DefaultRoomieRepository
import com.example.g46_kotlin.features.roomie.domain.repository.RoomieRepository
import com.example.g46_kotlin.features.roomie.domain.usecase.GetRecommendedRoomiesUseCase
import com.example.g46_kotlin.features.roomie.domain.usecase.SubmitRoomieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomieModule {
    @Provides
    @Singleton
    fun provideRoomieRepository(
        defaultRoomieRepository: DefaultRoomieRepository
    ): RoomieRepository = defaultRoomieRepository

    @Provides
    @Singleton
    fun provideGetRecommendedRoomiesUseCase(
        repository: RoomieRepository
    ): GetRecommendedRoomiesUseCase = GetRecommendedRoomiesUseCase(repository)

    @Provides
    @Singleton
    fun provideSubmitRoomieUseCase(
        repository: RoomieRepository
    ): SubmitRoomieUseCase = SubmitRoomieUseCase(repository)
}