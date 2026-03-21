package com.example.g46_kotlin.core.network

import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val path = original.url.encodedPath

        val isPublicAuthEndpoint =
            path.contains("/auth/login") || path.contains("/auth/refresh")

        if (isPublicAuthEndpoint) {
            return chain.proceed(original)
        }

        val accessToken = runBlocking { tokenStorage.getAccessToken() }

        if (accessToken.isNullOrBlank()) {
            return chain.proceed(original)
        }

        val authorizedRequest = original.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(authorizedRequest)
    }
}
