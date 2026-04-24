package com.example.g46_kotlin.features.favorites.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPrefsFavoritesSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    private val _favoriteIds = MutableStateFlow(
        prefs.getStringSet("favorite_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
    )
    val favoriteIds: Flow<Set<String>> = _favoriteIds.asStateFlow()

    suspend fun toggleFavorite(propertyId: String) = withContext(Dispatchers.IO) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(propertyId)) current.remove(propertyId) else current.add(propertyId)
        prefs.edit { putStringSet("favorite_ids", current) }
        _favoriteIds.value = current
    }

    fun isFavorite(propertyId: String): Boolean = _favoriteIds.value.contains(propertyId)
}
