package com.example.g46_kotlin.features.chat.di

import com.example.g46_kotlin.features.chat.data.repository.DefaultChatRepository
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        defaultChatRepository: DefaultChatRepository
    ): ChatRepository = defaultChatRepository
}