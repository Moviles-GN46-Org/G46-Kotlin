package com.example.g46_kotlin.features.notifications.domain.repository

import com.example.g46_kotlin.features.notifications.domain.model.Notification

interface NotificationsRepository {

    suspend fun getNotifications(): List<Notification>

    suspend fun readNotification(id: String): Boolean

    suspend fun readAllNotifications(): Boolean

    //TODO: Ponerle un nombre mas creativo a esto
    suspend fun createContextAwareNotification(title: String, body: String, propertyId: String, data: Map<String, String>, propertyImage: String): Boolean
}