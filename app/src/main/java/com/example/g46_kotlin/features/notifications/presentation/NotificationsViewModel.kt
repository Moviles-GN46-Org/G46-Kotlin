package com.example.g46_kotlin.features.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.notifications.domain.model.Notification
import com.example.g46_kotlin.features.notifications.domain.model.NotificationPayload
import com.example.g46_kotlin.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadAllNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val readNotificationsUseCase: ReadNotificationsUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun onRetry() = loadNotifications()
    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching { getNotificationsUseCase() }
                .onSuccess { notifications ->
                    val uiItems = notifications.map { it.toUi() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = uiItems,
                            unreadCount = uiItems.count { item -> !item.isRead },
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = emptyList(),
                            unreadCount = 0,
                            errorMessage = error.message ?: "Something went wrong"
                        )
                    }
                }
        }
    }

    fun onReadNotification(id: String) {
        viewModelScope.launch {
            runCatching { readNotificationsUseCase(id) }
                .onSuccess { ok ->
                    if (ok) {
                        _uiState.update { state ->
                            val updated = state.notifications.map { notification ->
                                if (notification.id == id) notification.markAsRead() else notification
                            }
                            state.copy(
                                notifications = updated,
                                unreadCount = updated.count { !it.isRead }
                            )
                        }
                    } else {
                        _uiState.update { it.copy(errorMessage = "Failed to mark notification as read") }
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(errorMessage = it.message ?: "Failed to mark notification as read")
                    }
                }
        }
    }

    fun onReadAll() {
        viewModelScope.launch {
            runCatching { readAllNotificationsUseCase() }
                .onSuccess { ok ->
                    if (ok) {
                        _uiState.update { state ->
                            if (state.unreadCount == 0) state
                            else state.copy(
                                notifications = state.notifications.map { it.markAsRead() },
                                unreadCount = 0
                            )
                        }
                    } else {
                        _uiState.update { it.copy(errorMessage = "Failed to mark all notifications as read") }
                    }
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(errorMessage = it.message ?: "Failed to mark all notifications as read")
                    }
                }
        }
    }

    private fun Notification.toUi(): NotificationCardModel {
        val created = createdAt.toMillisSafe()

        return when (val payload = data) {
            is NotificationPayload.ContextAware -> NotificationCardModel.ContextAware(
                id = id,
                title = title,
                message = body,
                createdAtMillis = created,
                isRead = isRead,
                propertyId = payload.propertyId,
                propertyImage = payload.propertyImage
            )

            is NotificationPayload.RoommateMatch -> NotificationCardModel.Match(
                id = id,
                title = title,
                message = body,
                createdAtMillis = created,
                isRead = isRead,
                chatId = payload.chatId
            )

            is NotificationPayload.ReviewReceived -> NotificationCardModel.Review(
                id = id,
                title = title,
                message = body,
                createdAtMillis = created,
                isRead = isRead,
                propertyId = payload.propertyId
            )

            is NotificationPayload.PropertyMatch -> NotificationCardModel.PropertyMatch(
                id = id,
                title = title,
                message = body,
                createdAtMillis = created,
                isRead = isRead,
                propertyId = payload.propertyId,
                landlordId = payload.landlordId,
                neighborhood = payload.neighborhood,
                monthlyRentL = payload.monthlyRentL
            )

            else -> NotificationCardModel.Generic(
                id = id,
                title = title,
                message = body,
                createdAtMillis = created,
                isRead = isRead
            )
        }
    }


    @OptIn(ExperimentalTime::class)
    private fun String.toMillisSafe(): Long{
        toLongOrNull()?.let { return it }
        return runCatching { Instant.parse(this).toEpochMilliseconds() }.getOrDefault(0L)
    }
}