package com.example.g46_kotlin.features.chat.data.remote

import com.example.g46_kotlin.features.chat.data.remote.dto.ChatResponse
import com.example.g46_kotlin.features.chat.data.remote.dto.ChatsListResponse
import com.example.g46_kotlin.features.chat.data.remote.dto.MessageResponse
import com.example.g46_kotlin.features.chat.data.remote.dto.MessagesListResponse
import com.example.g46_kotlin.features.chat.data.remote.dto.SendMessageRequestDto
import com.example.g46_kotlin.features.chat.data.remote.dto.StartChatRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @GET("chats")
    suspend fun listChats(): ChatsListResponse

    @POST("chats")
    suspend fun startChat(@Body body: StartChatRequestDto): ChatResponse

    @GET("chats/{id}/messages")
    suspend fun getMessages(
        @Path("id") chatId: String,
        @Query("after") after: String? = null
    ): MessagesListResponse

    @POST("chats/{id}/messages")
    suspend fun sendMessage(
        @Path("id") chatId: String,
        @Body body: SendMessageRequestDto
    ): MessageResponse
}