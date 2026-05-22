package com.example.g46_kotlin.features.chat.domain.usecase

import com.example.g46_kotlin.features.chat.domain.model.Message
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String, content: String): Message =
        repository.sendMessage(chatId, content)
}