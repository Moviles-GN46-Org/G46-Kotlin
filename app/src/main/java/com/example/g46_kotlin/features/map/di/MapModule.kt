package com.example.g46_kotlin.features.map.di

import com.example.g46_kotlin.features.map.data.repository.DefaultMapRepository
import com.example.g46_kotlin.features.map.data.repository.FakeMapRepository
import com.example.g46_kotlin.features.map.domain.repository.MapRepository
import com.example.g46_kotlin.features.map.domain.usecase.GetNearbyApartmentsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {

    @Provides
    @Singleton
    fun provideMapRepository(
        defaultMapRepository: DefaultMapRepository
    ): MapRepository = defaultMapRepository

    @Provides
    @Singleton
    fun provideGetNearbyApartmentsUseCase(
        repository: MapRepository
    ): GetNearbyApartmentsUseCase = GetNearbyApartmentsUseCase(repository)
}
