package com.example.g46_kotlin.features.notifications.domain.model

data class InAppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: InAppNotificationType,
    val createdAtMillis: Long,
    val isRead: Boolean = false
)

//TODO: Ponerle un nombre mas creativo a la context aware
sealed interface InAppNotificationType {
    data class ContextAware(val propertyId: String): InAppNotificationType
}