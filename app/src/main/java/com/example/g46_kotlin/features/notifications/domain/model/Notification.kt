package com.example.g46_kotlin.features.notifications.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val body: String,
    val data: NotificationPayload,
    val isRead: Boolean,
    val createdAt: String
)

//TODO: implementar notificación para smart feature cuando este listo el backend
sealed interface NotificationPayload {
    data class VisitConfirmed(val visitId: String, val propertyId: String): NotificationPayload
    data class RoommateMatch(val matchId: String, val chatId: String): NotificationPayload
    data class ReviewReceived(val reviewId: String, val propertyId: String): NotificationPayload
    data class NewMessage(val chatId: String, val messageId: String): NotificationPayload
    data class ContextAware(val propertyId: String, val propertyTitle: String, val neighborhood: String, val distanceMeters: Int, val propertyImage: String): NotificationPayload
    data class PropertyMatch(val propertyId: String, val landlordId: String, val neighborhood: String, val monthlyRentL: Long): NotificationPayload
    data class Unknown(val rawType: String, val rawData: Map<String, String?>): NotificationPayload
}