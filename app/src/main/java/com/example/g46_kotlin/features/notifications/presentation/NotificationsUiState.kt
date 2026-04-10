package com.example.g46_kotlin.features.notifications.presentation

data class NotificationsUiState (
    val isLoading: Boolean = false,
    val notifications: List<NotificationCardModel> = emptyList(),
    val unreadCount: Int = 0,
    val showPanel: Boolean = false,
    val errorMessage: String? = null
)

sealed interface NotificationCardModel {
    val id: String
    val title: String
    val message: String
    val createdAtMillis: Long
    val isRead: Boolean

    fun markAsRead(): NotificationCardModel

    data class ContextAware(
        override val id: String,
        override val title: String,
        override val message: String,
        override val createdAtMillis: Long,
        override val isRead: Boolean,
        val propertyId: String,
        val propertyImage: String
    ) : NotificationCardModel {
        override fun markAsRead(): NotificationCardModel = copy(isRead = true)
    }

    data class Match(
        override val id: String,
        override val title: String,
        override val message: String,
        override val createdAtMillis: Long,
        override val isRead: Boolean,
        val chatId: String
    ) : NotificationCardModel {
        override fun markAsRead(): NotificationCardModel = copy(isRead = true)
    }

    data class Review(
        override val id: String,
        override val title: String,
        override val message: String,
        override val createdAtMillis: Long,
        override val isRead: Boolean,
        val propertyId: String
    ) : NotificationCardModel {
        override fun markAsRead(): NotificationCardModel = copy(isRead = true)
    }

    data class Generic(
        override val id: String,
        override val title: String,
        override val message: String,
        override val createdAtMillis: Long,
        override val isRead: Boolean
    ) : NotificationCardModel {
        override fun markAsRead(): NotificationCardModel = copy(isRead = true)
    }

    data class PropertyMatch(
        override val id: String,
        override val title: String,
        override val message: String,
        override val createdAtMillis: Long,
        override val isRead: Boolean,
        val propertyId: String,
        val landlordId: String,
        val neighborhood: String,
        val monthlyRent: Long?
    ): NotificationCardModel {
        override fun markAsRead(): NotificationCardModel = copy(isRead = true)
    }
}

