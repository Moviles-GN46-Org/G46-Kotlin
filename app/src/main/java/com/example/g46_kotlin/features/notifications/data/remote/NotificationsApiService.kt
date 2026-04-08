package com.example.g46_kotlin.features.notifications.data.remote

import com.example.g46_kotlin.features.notifications.data.remote.dto.NotificationsResponseDto
import com.example.g46_kotlin.features.notifications.data.remote.dto.ReadNotificationsResponse
import retrofit2.http.GET
import retrofit2.http.PATCH

interface NotificationsApiService {

    @GET("notifications")
    suspend fun getNotifications(): NotificationsResponseDto

    @PATCH("notifications/read-all")
    suspend fun readAllNotifications(): ReadNotificationsResponse

    @PATCH("notifications/{id}/read")
    suspend fun readNotification(id: String): ReadNotificationsResponse
}