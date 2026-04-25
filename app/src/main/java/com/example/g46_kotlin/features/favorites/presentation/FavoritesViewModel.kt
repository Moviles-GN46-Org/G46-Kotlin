package com.example.g46_kotlin.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.favorites.domain.usecase.GetFavoriteCardsUseCase
import com.example.g46_kotlin.features.favorites.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteCardsUseCase: GetFavoriteCardsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = getFavoriteCardsUseCase()
        .map { FavoritesUiState(favoriteCards = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoritesUiState())

    fun onToggleFavorite(card: HousingCardUi) {
        viewModelScope.launch { toggleFavoriteUseCase(card) }
    }
}