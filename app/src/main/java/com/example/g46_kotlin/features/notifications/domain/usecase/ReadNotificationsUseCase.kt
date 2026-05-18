package com.example.g46_kotlin.features.notifications.domain.usecase

import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository

class ReadNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(
        id: String
    ): Boolean {
        return notificationsRepository.readNotification(id)
    }
}