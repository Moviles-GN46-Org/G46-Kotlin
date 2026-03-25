package com.example.g46_kotlin.features.auth.data.local

import kotlinx.coroutines.flow.Flow

interface TokenStorage {

    val accessTokenFlow: Flow<String?>
    suspend fun saveSession(accessToken: String, refreshToken: String?, persistent: Boolean)

    suspend fun updateSession(accessToken: String?, refreshToken: String?)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
}
