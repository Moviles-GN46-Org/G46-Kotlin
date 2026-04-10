package com.example.g46_kotlin.features.notifications.data.repository

import com.example.g46_kotlin.features.notifications.data.mapper.NotificationsMapper
import com.example.g46_kotlin.features.notifications.data.remote.NotificationsApiService
import com.example.g46_kotlin.features.notifications.domain.model.Notification
import com.example.g46_kotlin.features.notifications.domain.model.NotificationPayload
import com.example.g46_kotlin.features.notifications.domain.model.NotificationType
import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.mutableListOf

class DefaultNotificationsRepository @Inject constructor(
    private val notificationsApiService: NotificationsApiService,
    private val notificationsMapper: NotificationsMapper
): NotificationsRepository {

    private val inMemoryNotifications = mutableListOf<Notification>()
    private val cacheLock = Any()

    //TODO: Organizar luego para que tenga mejor orden que primer local y luego remotas
    override suspend fun getNotifications(): List<Notification> {
        val response = notificationsApiService.getNotifications()
        val remote = response.data.map { notificationsMapper.toDomain(it) }

        val localSnapshot = synchronized(cacheLock) {
            inMemoryNotifications.toList()
        }

        return localSnapshot + remote
    }

    override suspend fun readNotification(id: String): Boolean {
        val wasLocal = synchronized(cacheLock) {
            val idx = inMemoryNotifications.indexOfFirst { it.id == id }
            if (idx != -1) {
                inMemoryNotifications[idx] = inMemoryNotifications[idx].copy(isRead = true)
                true
            } else {
                false
            }
        }

        if (wasLocal) return true

        val response = notificationsApiService.readNotification(id)
        return response.success
    }

    override suspend fun readAllNotifications(): Boolean {
        synchronized(cacheLock) {
            for (i in inMemoryNotifications.indices) {
                inMemoryNotifications[i] = inMemoryNotifications[i].copy(isRead = true)
            }
        }

        val response = notificationsApiService.readAllNotifications()
        return response.success
    }

    override suspend fun createContextAwareNotification(
        title: String,
        body: String,
        propertyId: String,
        data: Map<String, String>,
        propertyImage: String
    ): Boolean {
        val alreadyExists = synchronized(cacheLock) {
            inMemoryNotifications.any { notification ->
                val payload = notification.data as? NotificationPayload.ContextAware
                payload?.propertyId == propertyId
            }
        }

        if (alreadyExists) return false
        val notification = Notification(
            id = "local-${UUID.randomUUID()}",
            userId = "local-user",
            type = NotificationType.CONTEXT_AWARE,
            title = title,
            body = body,
            data = NotificationPayload.ContextAware(
                propertyId = propertyId,
                propertyTitle = data["propertyTitle"] ?: "",
                neighborhood = data["neighborhood"] ?: "",
                distanceMeters = data["distanceMeters"]?.toIntOrNull() ?: 0,
                propertyImage = propertyImage
            ),
            isRead = false,
            //TODO: averiguar bien luego para usar Instant y mantener mismo formato que backend
            createdAt = System.currentTimeMillis().toString()
        )

        synchronized(cacheLock) {
            inMemoryNotifications.add(0, notification)
        }

        return true
    }
}