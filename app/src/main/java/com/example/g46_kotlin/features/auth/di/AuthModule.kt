package com.example.g46_kotlin.features.auth.di


import com.example.g46_kotlin.features.auth.data.repository.FakeAuthRepository
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository
import com.example.g46_kotlin.features.auth.domain.usecase.LoginWithEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = FakeAuthRepository()

    @Provides
    @Singleton
    fun provideLoginWithEmailUseCase(
        repository: AuthRepository
    ): LoginWithEmailUseCase = LoginWithEmailUseCase(repository)
}