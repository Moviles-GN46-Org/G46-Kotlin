package com.example.g46_kotlin.features.house.di

import com.example.g46_kotlin.features.house.data.repository.FakeHouseRepository
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import com.example.g46_kotlin.features.house.domain.usecase.GetHouseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HouseModule {

    @Provides
    @Singleton
    fun provideHouseRepository(): HouseRepository = FakeHouseRepository()

    @Provides
    @Singleton
    fun provideGetHouseUseCase(
        repository: HouseRepository
    ): GetHouseUseCase = GetHouseUseCase(repository)
}