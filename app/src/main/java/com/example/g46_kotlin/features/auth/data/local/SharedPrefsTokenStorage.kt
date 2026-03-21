package com.example.g46_kotlin.features.auth.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class SharedPrefsTokenStorage @Inject constructor(
    @ApplicationContext context: Context
) : TokenStorage {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override suspend fun saveAccessToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit { putString("access_token", token) }
    }

    override suspend fun saveRefreshToken(token: String?) = withContext(Dispatchers.IO) {
        prefs.edit { putString("refresh_token", token) }
    }

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString("access_token", null)
    }

    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString("refresh_token", null)
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit { clear() }
    }
}
