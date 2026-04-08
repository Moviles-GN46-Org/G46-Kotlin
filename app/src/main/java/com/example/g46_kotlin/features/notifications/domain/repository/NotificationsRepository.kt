package com.example.g46_kotlin.features.notifications.domain.repository

import com.example.g46_kotlin.features.notifications.domain.model.Notification

interface NotificationsRepository {

    suspend fun getNotifications(): List<Notification>

    suspend fun readNotification(id: String): Boolean

    suspend fun readAllNotifications(): Boolean
}