package com.example.g46_kotlin.core.utils

import android.util.Base64

object JwtUtils {
    fun extractUserId(token: String): String? = runCatching {
        val payload = token.split(".").getOrNull(1) ?: return null
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING)
        val json = String(decoded, Charsets.UTF_8)
        Regex(""""userId"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1)
    }.getOrNull()
}