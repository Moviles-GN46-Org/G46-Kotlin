package com.example.g46_kotlin.core.network

import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import com.example.g46_kotlin.features.auth.data.remote.AuthRefreshApiService
import com.example.g46_kotlin.features.auth.data.remote.dto.RefreshTokenDataDto
import com.example.g46_kotlin.features.auth.data.remote.dto.RefreshTokenRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val refreshApi: AuthRefreshApiService
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val path = response.request.url.encodedPath

        if (path.contains("/auth/login") || path.contains("/auth/refresh")) return null
        if (responseCount(response) >= 2) return null

        val refreshToken = runBlocking { tokenStorage.getRefreshToken() }

        if (refreshToken.isNullOrBlank()){
            runBlocking { tokenStorage.clear() }
            return null
        }

        val oldAccess = runBlocking { tokenStorage.getAccessToken() }

        val requestAccess = response.request.header("Authorization")
            ?.removePrefix("Bearer ")
            ?.trim()

        if (!oldAccess.isNullOrBlank() && oldAccess != requestAccess){
            return response.request.newBuilder()
                .header("Authorization", "Bearer $oldAccess")
                .build()
        }

        val refreshResponse = runBlocking {
            runCatching {
                refreshApi.refreshToken(RefreshTokenRequestDto(refreshToken))
            }.getOrNull()
        } ?: run {
            runBlocking { tokenStorage.clear() }
            return null
        }

        val newAccess = refreshResponse.data.accessToken
        val newRefresh = refreshResponse.data.refreshToken

        if (newAccess.isNullOrBlank()) {
            runBlocking { tokenStorage.clear() }
            return null
        }

        runBlocking {
            tokenStorage.updateSession(
                accessToken = newAccess,
                refreshToken = newRefresh
            )
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var current = response.priorResponse
        while (current != null) {
            count++
            current = current.priorResponse
        }
        return count
    }
}

