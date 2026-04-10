package com.example.g46_kotlin.core.domain.contract

interface NotificationPublisher {
    suspend fun publishContextAware(
        propertyTitle: String,
        neighborhood: String,
        distanceMeters: Int,
        propertyId: String,
        propertyImage: String
    ): Boolean
}