package com.example.g46_kotlin.features.auth.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedPrefsTokenStorage @Inject constructor(
    @ApplicationContext context: Context
) : TokenStorage {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val _accessTokenFlow = MutableStateFlow(prefs.getString("access_token", null))
    override val accessTokenFlow: Flow<String?> = _accessTokenFlow.asStateFlow()

    private var volatileAccessToken: String? = null
    private var volatileRefreshToken: String? = null

    private var isVolatileSessionActive: Boolean = false


    override suspend fun saveSession(
        accessToken: String,
        refreshToken: String?,
        persistent: Boolean
    ) {
        if (persistent) {
            volatileAccessToken = null
            volatileRefreshToken = null
            isVolatileSessionActive = false
            withContext(Dispatchers.IO) {
                prefs.edit {
                    putString("access_token", accessToken)
                    putString("refresh_token", refreshToken)
                }
            }
        } else {
            isVolatileSessionActive = true
            withContext(Dispatchers.IO) {
                prefs.edit {
                    remove("access_token")
                    remove("refresh_token")
                }
            }
            volatileAccessToken = accessToken
            volatileRefreshToken = refreshToken
        }
        _accessTokenFlow.value = accessToken
    }

    override suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        if (isVolatileSessionActive) {
            return@withContext volatileAccessToken
        } else {
            return@withContext prefs.getString("access_token", null)
        }

    }

    override suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        if (isVolatileSessionActive) {
            return@withContext volatileRefreshToken
        } else {
            return@withContext prefs.getString("refresh_token", null)
        }
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit {
            remove("access_token")
            remove("refresh_token")
        }
        volatileAccessToken = null
        volatileRefreshToken = null
        isVolatileSessionActive = false
        _accessTokenFlow.value = null
    }

    override suspend fun updateSession(accessToken: String?, refreshToken: String?) {
        if (isVolatileSessionActive) {
            volatileAccessToken = accessToken
            volatileRefreshToken = refreshToken
        } else {
            withContext(Dispatchers.IO) {
                prefs.edit {
                    putString("access_token", accessToken)
                    putString("refresh_token", refreshToken)
                }
            }
        }
        _accessTokenFlow.value = accessToken
    }
}
