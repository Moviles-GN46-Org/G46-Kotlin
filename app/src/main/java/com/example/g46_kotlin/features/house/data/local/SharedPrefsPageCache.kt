package com.example.g46_kotlin.features.house.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.g46_kotlin.cards.HousingCardUi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsPageCache @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("house_page_cache_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val MAX_CACHED_PAGES = 5
        private const val CACHE_KEY = "cached_pages"
    }

    @Serializable
    private data class CachedPage(
        val pageNumber: Int,
        val items: List<HousingCardUi>
    )

    private fun load(): MutableList<CachedPage> {
        val raw = prefs.getString(CACHE_KEY, null) ?: return mutableListOf()
        return runCatching {
            json.decodeFromString<List<CachedPage>>(raw).toMutableList()
        }.getOrDefault(mutableListOf())
    }

    private fun save(pages: List<CachedPage>) {
        prefs.edit { putString(CACHE_KEY, json.encodeToString(pages)) }
    }

    fun savePage(pageNumber: Int, items: List<HousingCardUi>) {
        val pages = load()
        pages.removeAll { it.pageNumber == pageNumber }
        pages.add(CachedPage(pageNumber, items))
        if (pages.size > MAX_CACHED_PAGES) pages.removeAt(0)
        save(pages)
    }

    fun getPage(pageNumber: Int): List<HousingCardUi>? =
        load().find { it.pageNumber == pageNumber }?.items

    fun getCachedPageNumbers(): Set<Int> =
        load().map { it.pageNumber }.toSet()
}