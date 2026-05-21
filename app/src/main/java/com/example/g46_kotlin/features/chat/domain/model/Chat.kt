package com.example.g46_kotlin.features.chat.domain.model

data class ChatParticipant(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val profilePictureUrl: String?
)

data class ChatProperty(
    val id: String,
    val title: String,
    val imageUrls: List<String>
)

data class Chat(
    val id: String,
    val propertyId: String,
    val property: ChatProperty?,
    val status: String,
    val participants: List<ChatParticipant>,
    val lastMessage: Message?,
    val createdAt: String,
    val updatedAt: String
)