package com.example.g46_kotlin.features.notifications.domain.usecase

import com.example.g46_kotlin.features.notifications.domain.repository.NotificationsRepository
import java.util.Locale

class CreateContextAwareNotificationUseCase(
    private val notificationsRepository: NotificationsRepository
) {
    suspend operator fun invoke(
        propertyTitle: String,
        neighborhood: String,
        distanceMeters: Int,
        propertyId: String,
        propertyImage: String
    ): Boolean {
        val title = "Property near you: $propertyTitle"
        val body = "$propertyTitle in $neighborhood is ${formatDistance(distanceMeters)} away"
        val data = mapOf(
            "propertyId" to propertyId,
            "propertyTitle" to propertyTitle,
            "neighborhood" to neighborhood,
            "distanceMeters" to distanceMeters.toString(),
            "propertyImage" to propertyImage
        )

        return notificationsRepository.createContextAwareNotification(
            title = title,
            body = body,
            propertyId = propertyId,
            data = data,
            propertyImage = propertyImage
        )
    }

    private fun formatDistance(distanceMeters: Int): String {
        return if (distanceMeters < 1000) {
            "$distanceMeters m"
        } else {
            String.format(Locale.US, "%.1f km", distanceMeters / 1000.0)
        }
    }
}

