package com.example.g46_kotlin.features.notifications.domain.usecase

import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository

class ReadAllNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(): Boolean {
        return notificationsRepository.readAllNotifications()
    }
}