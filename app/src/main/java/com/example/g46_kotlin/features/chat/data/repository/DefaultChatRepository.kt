package com.example.g46_kotlin.features.chat.data.repository

import com.example.g46_kotlin.features.chat.data.local.SharedPrefsChatCache
import com.example.g46_kotlin.features.chat.data.remote.ChatApiService
import com.example.g46_kotlin.features.chat.data.remote.dto.ChatDto
import com.example.g46_kotlin.features.chat.data.remote.dto.MessageDto
import com.example.g46_kotlin.features.chat.data.remote.dto.SendMessageRequestDto
import com.example.g46_kotlin.features.chat.data.remote.dto.StartChatRequestDto
import com.example.g46_kotlin.features.chat.domain.model.Chat
import com.example.g46_kotlin.features.chat.domain.model.ChatParticipant
import com.example.g46_kotlin.features.chat.domain.model.ChatProperty
import com.example.g46_kotlin.features.chat.domain.model.Message
import com.example.g46_kotlin.features.chat.domain.model.MessageSender
import com.example.g46_kotlin.features.chat.domain.model.MessageStatus
import com.example.g46_kotlin.features.chat.domain.model.MessageType
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultChatRepository @Inject constructor(
    private val api: ChatApiService,
    private val cache: SharedPrefsChatCache
) : ChatRepository {

    override suspend fun listChats(): List<Chat> {
        val chats = api.listChats().data
        cache.saveChats(chats)
        return chats.map { it.toDomain() }
    }

    override suspend fun startChat(propertyId: String): Chat {
        return api.startChat(StartChatRequestDto(propertyId)).data.toDomain()
    }

    override suspend fun getMessages(chatId: String, after: String?): List<Message> {
        val incoming = api.getMessages(chatId, after).data
        val merged = if (after == null) {
            incoming
        } else {
            val existing = cache.loadMessages(chatId)
            val existingIds = existing.map { it.id }.toSet()
            existing + incoming.filter { it.id !in existingIds }
        }
        cache.saveMessages(chatId, merged)
        return merged.map { it.toDomain() }
    }

    override suspend fun sendMessage(chatId: String, content: String): Message {
        return api.sendMessage(chatId, SendMessageRequestDto(content)).data.toDomain()
    }

    override fun getCachedChats(): List<Chat> = cache.loadChats().map { it.toDomain() }

    override fun getCachedMessages(chatId: String): List<Message> =
        cache.loadMessages(chatId).map { it.toDomain() }

    private fun ChatDto.toDomain() = Chat(
        id = id,
        propertyId = propertyId,
        property = property?.let { ChatProperty(it.id, it.title, it.imageUrls) },
        status = status,
        participants = participants.map {
            ChatParticipant(it.id, it.firstName, it.lastName, it.role, it.profilePictureUrl)
        },
        lastMessage = lastMessage?.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun MessageDto.toDomain() = Message(
        id = id,
        chatId = chatId,
        sender = MessageSender(
            id = sender?.id ?: "",
            firstName = sender?.firstName ?: "",
            lastName = sender?.lastName ?: "",
            profilePictureUrl = sender?.profilePictureUrl
        ),
        type = when (type) {
            "TEXT" -> MessageType.TEXT
            "VISIT_PROPOSAL" -> MessageType.VISIT_PROPOSAL
            "VISIT_RESPONSE" -> MessageType.VISIT_RESPONSE
            else -> MessageType.UNKNOWN
        },
        content = content,
        isRead = isRead,
        createdAt = createdAt,
        status = MessageStatus.SENT
    )
}