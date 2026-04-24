package com.example.g46_kotlin.features.house.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import com.example.g46_kotlin.features.house.domain.usecase.GetPropertyByIdUseCase
import com.example.g46_kotlin.features.house.presentation.mapper.PropertyDetailUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val propertyDetailUiMapper: PropertyDetailUiMapper,
    private val favoritesRepository: FavoritesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val propertyId: String = checkNotNull(savedStateHandle["propertyId"]) {
        "propertyId is required in navigation arguments"
    }

    private val _uiState = MutableStateFlow(PropertyDetailUiState(isLoading = true))
    val uiState: StateFlow<PropertyDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoritesRepository.getFavoriteIds()
        .map { ids -> propertyId in ids }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = favoritesRepository.isFavorite(propertyId)
        )

    init {
        loadProperty()
    }

    fun loadProperty() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getPropertyByIdUseCase(propertyId) }
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(isLoading = false, detail = propertyDetailUiMapper.toUi(detail))
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = throwable.message ?: "Unknown error")
                    }
                }
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(propertyId)
        }
    }
}