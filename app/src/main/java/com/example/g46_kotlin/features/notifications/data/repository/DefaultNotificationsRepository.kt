package com.example.g46_kotlin.features.notifications.data.repository

import com.example.g46_kotlin.features.notifications.data.mapper.NotificationsMapper
import com.example.g46_kotlin.features.notifications.data.remote.NotificationsApiService
import com.example.g46_kotlin.features.notifications.domain.model.Notification
import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository
import javax.inject.Inject

class DefaultNotificationsRepository @Inject constructor(
    private val notificationsApiService: NotificationsApiService,
    private val notificationsMapper: NotificationsMapper
): NotificationsRepository {
    override suspend fun getNotifications(): List<Notification> {
        val response = notificationsApiService.getNotifications()
        return response.data.map { notificationsMapper.toDomain(it) }
    }

    override suspend fun readNotification(id: String): Boolean {
        val response = notificationsApiService.readNotification(id)
        return response.success
    }

    override suspend fun readAllNotifications(): Boolean {
        val response = notificationsApiService.readAllNotifications()
        return response.success
    }

}