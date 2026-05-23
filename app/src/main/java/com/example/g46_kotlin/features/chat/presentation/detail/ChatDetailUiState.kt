package com.example.g46_kotlin.features.chat.presentation.detail

import com.example.g46_kotlin.features.chat.domain.model.MessageStatus
import com.example.g46_kotlin.features.chat.domain.model.MessageType

data class MessageUi(
    val id: String,
    val content: String,
    val isFromMe: Boolean,
    val type: MessageType,
    val time: String,
    val createdAt: String,
    val status: MessageStatus
)

data class ChatDetailUiState(
    val isLoading: Boolean = false,
    val chatName: String = "",
    val chatAvatar: String? = null,
    val propertyId: String? = null,
    val propertyTitle: String? = null,
    val propertyImageUrl: String? = null,
    val messages: List<MessageUi> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)