package com.example.g46_kotlin.features.chat.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.g46_kotlin.features.chat.data.remote.dto.ChatDto
import com.example.g46_kotlin.features.chat.data.remote.dto.MessageDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsChatCache @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("chat_cache_prefs", Context.MODE_PRIVATE)
    @Suppress("unused")
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun saveChats(chats: List<ChatDto>) = withContext(Dispatchers.IO) {
        prefs.edit { putString(KEY_CHATS, json.encodeToString(chats)) }
    }

    fun loadChats(): List<ChatDto> {
        val raw = prefs.getString(KEY_CHATS, null) ?: return emptyList()
        return runCatching { json.decodeFromString<List<ChatDto>>(raw) }.getOrDefault(emptyList())
    }

    suspend fun saveMessages(chatId: String, messages: List<MessageDto>) = withContext(Dispatchers.IO) {
        prefs.edit { putString(messagesKey(chatId), json.encodeToString(messages)) }
    }

    fun loadMessages(chatId: String): List<MessageDto> {
        val raw = prefs.getString(messagesKey(chatId), null) ?: return emptyList()
        return runCatching { json.decodeFromString<List<MessageDto>>(raw) }.getOrDefault(emptyList())
    }

    private fun messagesKey(chatId: String) = "messages_$chatId"

    companion object {
        private const val KEY_CHATS = "cached_chats"
    }
}