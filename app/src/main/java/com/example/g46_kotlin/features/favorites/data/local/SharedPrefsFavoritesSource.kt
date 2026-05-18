package com.example.g46_kotlin.features.favorites.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.core.utils.JwtUtils
import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsFavoritesSource @Inject constructor(
    @ApplicationContext context: Context,
    tokenStorage: TokenStorage
) {
    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true }

    private fun keyForUser(userId: String) = "favorite_cards_$userId"

    private fun loadForUser(userId: String): Map<String, HousingCardUi> {
        val raw = prefs.getString(keyForUser(userId), null) ?: return emptyMap()
        return runCatching { json.decodeFromString<Map<String, HousingCardUi>>(raw) }.getOrDefault(emptyMap())
    }

    private val initialUserId: String = run {
        val authPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = authPrefs.getString("access_token", null)
        token?.let { JwtUtils.extractUserId(it) } ?: "guest"
    }

    @Volatile private var currentUserId: String = initialUserId

    private val _favoriteCards = MutableStateFlow(loadForUser(initialUserId))

    val favoriteCards: Flow<List<HousingCardUi>> = _favoriteCards.asStateFlow().map { it.values.toList() }
    val favoriteIds: Flow<Set<String>> = _favoriteCards.asStateFlow().map { it.keys }

    init {
        scope.launch {
            tokenStorage.accessTokenFlow.collect { token ->
                val newUserId = token?.let { JwtUtils.extractUserId(it) } ?: "guest"
                if (newUserId != currentUserId) {
                    currentUserId = newUserId
                    _favoriteCards.value = loadForUser(newUserId)
                }
            }
        }
    }

    suspend fun toggleFavorite(card: HousingCardUi) = withContext(Dispatchers.IO) {
        val current = _favoriteCards.value.toMutableMap()
        if (current.containsKey(card.id)) current.remove(card.id) else current[card.id] = card
        prefs.edit { putString(keyForUser(currentUserId), json.encodeToString(current)) }
        _favoriteCards.value = current
    }

    fun isFavorite(propertyId: String): Boolean = _favoriteCards.value.containsKey(propertyId)
}