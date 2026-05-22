package com.example.g46_kotlin.features.chat.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ChatParticipantDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val profilePictureUrl: String? = null
)

@Serializable
data class ChatPropertyDto(
    val id: String,
    val title: String,
    val imageUrls: List<String> = emptyList()
)

@Serializable
data class MessageSenderDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String? = null
)

@Serializable
data class MessageDto(
    val id: String,
    val chatId: String,
    val sender: MessageSenderDto? = null,
    val type: String,
    val content: String,
    val metadata: JsonElement? = null,
    val isRead: Boolean = false,
    val createdAt: String
)

@Serializable
data class ChatDto(
    val id: String,
    val propertyId: String,
    val property: ChatPropertyDto? = null,
    val status: String,
    val participants: List<ChatParticipantDto> = emptyList(),
    val lastMessage: MessageDto? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class ChatsListResponse(
    val success: Boolean,
    val data: List<ChatDto>
)

@Serializable
data class ChatResponse(
    val success: Boolean,
    val data: ChatDto
)

@Serializable
data class MessagesListResponse(
    val success: Boolean,
    val data: List<MessageDto>
)

@Serializable
data class MessageResponse(
    val success: Boolean,
    val data: MessageDto
)

@Serializable
data class StartChatRequestDto(
    val propertyId: String
)

@Serializable
data class SendMessageRequestDto(
    val content: String,
    val type: String = "TEXT"
)