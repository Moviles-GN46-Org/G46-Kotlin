package com.example.g46_kotlin.features.favorites.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.g46_kotlin.core.utils.JwtUtils
import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsFavoritesSource @Inject constructor(
    @ApplicationContext context: Context,
    tokenStorage: TokenStorage
) {
    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun keyForUser(userId: String) = "favorite_ids_$userId"

    private fun loadFavoritesForUser(userId: String): MutableSet<String> =
        prefs.getStringSet(keyForUser(userId), emptySet())?.toMutableSet() ?: mutableSetOf()

    private val initialUserId: String = run {
        val authPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = authPrefs.getString("access_token", null)
        token?.let { JwtUtils.extractUserId(it) } ?: "guest"
    }

    @Volatile private var currentUserId: String = initialUserId

    private val _favoriteIds = MutableStateFlow(loadFavoritesForUser(initialUserId))
    val favoriteIds: Flow<Set<String>> = _favoriteIds.asStateFlow()

    init {
        scope.launch {
            tokenStorage.accessTokenFlow.collect { token ->
                val newUserId = token?.let { JwtUtils.extractUserId(it) } ?: "guest"
                if (newUserId != currentUserId) {
                    currentUserId = newUserId
                    _favoriteIds.value = loadFavoritesForUser(newUserId)
                }
            }
        }
    }

    suspend fun toggleFavorite(propertyId: String) = withContext(Dispatchers.IO) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(propertyId)) current.remove(propertyId) else current.add(propertyId)
        prefs.edit { putStringSet(keyForUser(currentUserId), current) }
        _favoriteIds.value = current
    }

    fun isFavorite(propertyId: String): Boolean = _favoriteIds.value.contains(propertyId)
}