package com.example.g46_kotlin.features.house.di

import com.example.g46_kotlin.features.house.data.repository.DefaultHouseRepository
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import com.example.g46_kotlin.features.house.domain.usecase.GetHouseUseCase
import com.example.g46_kotlin.features.house.domain.usecase.GetPropertyByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HouseModule {

    @Provides
    @Singleton
    fun provideHouseRepository(
        defaultHouseRepository: DefaultHouseRepository
    ): HouseRepository = defaultHouseRepository

    @Provides
    @Singleton
    fun provideGetHouseUseCase(
        repository: HouseRepository
    ): GetHouseUseCase = GetHouseUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPropertyByIdUseCase(
        repository: HouseRepository
    ): GetPropertyByIdUseCase = GetPropertyByIdUseCase(repository)
}