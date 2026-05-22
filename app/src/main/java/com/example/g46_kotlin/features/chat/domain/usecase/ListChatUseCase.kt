package com.example.g46_kotlin.features.chat.domain.usecase

import com.example.g46_kotlin.features.chat.domain.model.Chat
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class ListChatsUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(): List<Chat> = repository.listChats()
}