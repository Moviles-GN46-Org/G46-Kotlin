package com.example.g46_kotlin.features.notifications.domain.publisher

import com.example.g46_kotlin.core.domain.contract.NotificationPublisher
import com.example.g46_kotlin.features.notifications.domain.usecase.CreateContextAwareNotificationUseCase
import javax.inject.Inject

class InAppNotificationPublisher @Inject constructor(
    private val createContextAwareNotificationUseCase: CreateContextAwareNotificationUseCase
): NotificationPublisher {
    override suspend fun publishContextAware(
        propertyTitle: String,
        neighborhood: String,
        distanceMeters: Int,
        propertyId: String,
        propertyImage: String
    ): Boolean {
        return createContextAwareNotificationUseCase(
            propertyTitle = propertyTitle,
            neighborhood = neighborhood,
            distanceMeters = distanceMeters,
            propertyId = propertyId,
            propertyImage = propertyImage
        )
    }
}