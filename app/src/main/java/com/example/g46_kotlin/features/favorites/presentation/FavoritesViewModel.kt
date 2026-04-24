package com.example.g46_kotlin.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.favorites.domain.usecase.GetFavoriteIdsUseCase
import com.example.g46_kotlin.features.favorites.domain.usecase.ToggleFavoriteUseCase
import com.example.g46_kotlin.features.house.domain.usecase.GetPropertyByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private var loadCardsJob: Job? = null

    init {
        viewModelScope.launch {
            getFavoriteIdsUseCase().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
                loadCardsForIds(ids)
            }
        }
    }

    private fun loadCardsForIds(ids: Set<String>) {
        loadCardsJob?.cancel()
        if (ids.isEmpty()) {
            _uiState.update { it.copy(favoriteCards = emptyList(), isLoading = false) }
            return
        }
        loadCardsJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cards = ids.mapNotNull { id ->
                runCatching { getPropertyByIdUseCase(id) }.getOrNull()
            }.map { detail ->
                HousingCardUi(
                    id = detail.id,
                    name = detail.title,
                    pricePerMonth = detail.monthlyRent.toInt(),
                    rating = 0.0,
                    neighborhood = detail.neighborhood,
                    propertyType = "${detail.propertyType.name.replace("_", " ")} · ${detail.bedrooms} Bed · ${detail.bathrooms} Bath",
                    imageUrl = detail.imageUrls.firstOrNull()
                )
            }
            _uiState.update { it.copy(favoriteCards = cards, isLoading = false) }
        }
    }

    fun onToggleFavorite(propertyId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(propertyId)
        }
    }
}