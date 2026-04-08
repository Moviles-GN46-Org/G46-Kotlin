package com.example.g46_kotlin.features.notifications.domain.usecase

import com.example.g46_kotlin.features.notifications.domain.model.Notification
import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository

class GetNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(): List<Notification> {
        return notificationsRepository.getNotifications()
    }
}