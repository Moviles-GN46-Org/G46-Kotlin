package com.example.g46_kotlin.features.notifications.domain.model

enum class NotificationType(val raw: String) {
    VISIT_CONFIRMED("VISIT_CONFIRMED"),
    ROOMMATE_MATCH("ROOMMATE_MATCH"),
    REVIEW_RECEIVED("REVIEW_RECEIVED"),
    NEW_MESSAGE("NEW_MESSAGE"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromRaw(value: String): NotificationType =
            entries.firstOrNull { it.raw == value } ?: UNKNOWN
    }
}