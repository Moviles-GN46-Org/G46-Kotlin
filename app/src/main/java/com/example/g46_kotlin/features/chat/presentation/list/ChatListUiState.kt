package com.example.g46_kotlin.features.chat.presentation.list

data class ChatUi(
    val id: String,
    val otherParticipantName: String,
    val otherParticipantAvatar: String?,
    val propertyTitle: String?,
    val lastMessageContent: String?,
    val lastMessageTime: String?,
    val lastMessageIsRead: Boolean,
    val lastMessageIsFromMe: Boolean,
    val status: String
)

data class ChatListUiState(
    val isLoading: Boolean = false,
    val chats: List<ChatUi> = emptyList(),
    val errorMessage: String? = null,
    val isOffline: Boolean = false
)