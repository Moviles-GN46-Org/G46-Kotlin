package com.example.g46_kotlin.features.chat.domain.repository

import com.example.g46_kotlin.features.chat.domain.model.Chat
import com.example.g46_kotlin.features.chat.domain.model.Message

interface ChatRepository {
    suspend fun listChats(): List<Chat>
    suspend fun startChat(propertyId: String): Chat
    suspend fun getMessages(chatId: String, after: String? = null): List<Message>
    suspend fun sendMessage(chatId: String, content: String): Message
    fun getCachedChats(): List<Chat>
    fun getCachedMessages(chatId: String): List<Message>
}