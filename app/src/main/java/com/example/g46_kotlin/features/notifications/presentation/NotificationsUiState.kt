package com.example.g46_kotlin.features.notifications.presentation

data class NotificationsUiState (
    val isLoading: Boolean = false,
    val notifications: List<NotificationUi> = emptyList(),
    val unreadCount: Int = 0,
    val showPanel: Boolean = false,
    val errorMessage: String? = null
)

data class NotificationUi(
    val id: String,
    val title: String,
    val message: String,
    val createdAtMillis: Long,
    val isRead: Boolean
)