package com.example.g46_kotlin.features.chat.domain.model

enum class MessageType { TEXT, VISIT_PROPOSAL, VISIT_RESPONSE, UNKNOWN }

enum class MessageStatus { SENT, PENDING }

data class MessageSender(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String?
)

data class Message(
    val id: String,
    val chatId: String,
    val sender: MessageSender,
    val type: MessageType,
    val content: String,
    val isRead: Boolean,
    val createdAt: String,
    val status: MessageStatus = MessageStatus.SENT
)