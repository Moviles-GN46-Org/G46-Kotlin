package com.example.g46_kotlin.features.notifications.data.mapper

import com.example.g46_kotlin.features.notifications.data.remote.dto.NotificationDto
import com.example.g46_kotlin.features.notifications.domain.model.Notification
import com.example.g46_kotlin.features.notifications.domain.model.NotificationPayload
import com.example.g46_kotlin.features.notifications.domain.model.NotificationType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

class NotificationsMapper @Inject constructor() {

    fun toDomain(dto: NotificationDto): Notification {

        val type = NotificationType.fromRaw(dto.type)
        val payload = mapPayload(type, dto.type, dto.data)

        return Notification(
            id = dto.id,
            userId = dto.userId,
            type = type,
            title = dto.title,
            body = dto.body,
            data = payload,
            isRead = dto.isRead,
            createdAt = dto.createdAt
        )
    }

    //TODO: actualizar con notificación de propiedad nueva cuando este listo en backend
    private fun mapPayload(
        type: NotificationType,
        rawType: String,
        data: JsonObject
    ): NotificationPayload {
        return when (type) {
            NotificationType.VISIT_CONFIRMED -> {
                val visitId = data.s("visitId")
                val propertyId = data.s("propertyId")
                if (visitId != null && propertyId != null) {
                    NotificationPayload.VisitConfirmed(
                        visitId = visitId,
                        propertyId = propertyId
                    )
                } else {
                    unknown(rawType, data)
                }
            }

            NotificationType.ROOMMATE_MATCH -> {
                val matchId = data.s("matchId")
                val chatId = data.s("chatId")
                if (matchId != null && chatId != null) {
                    NotificationPayload.RoommateMatch(
                        matchId = matchId,
                        chatId = chatId
                    )
                } else {
                    unknown(rawType, data)
                }
            }

            NotificationType.REVIEW_RECEIVED -> {
                val reviewId = data.s("reviewId")
                val propertyId = data.s("propertyId")
                if (reviewId != null && propertyId != null) {
                    NotificationPayload.ReviewReceived(
                        reviewId = reviewId,
                        propertyId = propertyId
                    )
                } else {
                    unknown(rawType, data)
                }
            }

            NotificationType.NEW_MESSAGE -> {
                val chatId = data.s("chatId")
                val messageId = data.s("messageId")
                if (chatId != null && messageId != null) {
                    NotificationPayload.NewMessage(
                        chatId = chatId,
                        messageId = messageId
                    )
                } else {
                    unknown(rawType, data)
                }
            }

            NotificationType.UNKNOWN -> unknown(rawType, data)
        }
    }

    private fun unknown(rawType: String, data: JsonObject)
    : NotificationPayload.Unknown {
        return NotificationPayload.Unknown(
            rawType = rawType,
            rawData = data.mapValues { (_, value) ->
                value.jsonPrimitive.contentOrNull
            }
        )
    }

    private fun JsonObject.s(key: String): String? =
        this[key]?.jsonPrimitive?.contentOrNull
}