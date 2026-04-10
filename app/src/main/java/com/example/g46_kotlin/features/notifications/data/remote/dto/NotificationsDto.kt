package com.example.g46_kotlin.features.notifications.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class NotificationsResponseDto(
    val success: Boolean,
    val data: List<NotificationDto>
)

@Serializable
data class NotificationDto(
    val id: String,
    val userId: String,
    val type: String,
    val title: String,
    val body: String,
    val data: JsonObject,
    val isRead: Boolean,
    val createdAt: String
)

@Serializable
data class ReadNotificationsResponse(
    val success: Boolean,
    val data: JsonObject
) {
    val message: String?
        get() = data["message"]?.jsonPrimitive?.contentOrNull
}