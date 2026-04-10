package com.example.g46_kotlin.features.notifications.di

import com.example.g46_kotlin.core.domain.contract.NotificationPublisher
import com.example.g46_kotlin.features.notifications.data.repository.DefaultNotificationsRepository
import com.example.g46_kotlin.features.notifications.domain.publisher.InAppNotificationPublisher
import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository
import com.example.g46_kotlin.features.notifications.domain.usecase.CreateContextAwareNotificationUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadAllNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadNotificationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Provides
    @Singleton
    fun provideNotificationsRepository(
        defaultNotificationsRepository: DefaultNotificationsRepository
    ): NotificationsRepository = defaultNotificationsRepository

    @Provides
    @Singleton
    fun provideNotificationPublisher(
        inAppNotificationPublisher: InAppNotificationPublisher
    ): NotificationPublisher = inAppNotificationPublisher

    @Provides
    @Singleton
    fun provideGetNotificationsUseCase(
        repository: NotificationsRepository
    ): GetNotificationsUseCase = GetNotificationsUseCase(repository)

    @Provides
    @Singleton
    fun provideReadNotificationsUseCase(
        repository: NotificationsRepository
    ): ReadNotificationsUseCase = ReadNotificationsUseCase(repository)

    @Provides
    @Singleton
    fun provideReadAllNotificationsUseCase(
        repository: NotificationsRepository
    ): ReadAllNotificationsUseCase = ReadAllNotificationsUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateContextAwareNotificationUseCase(
        repository: NotificationsRepository
    ): CreateContextAwareNotificationUseCase = CreateContextAwareNotificationUseCase(repository)
}