package com.example.g46_kotlin.features.house.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.favorites.domain.repository.FavoritesRepository
import com.example.g46_kotlin.features.house.domain.usecase.GetPropertyByIdUseCase
import com.example.g46_kotlin.features.house.presentation.mapper.PropertyDetailUiMapper
import com.example.g46_kotlin.features.analytics.data.repository.AnalyticsRepository
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
import com.example.g46_kotlin.core.domain.contract.NetworkMonitor
import kotlinx.coroutines.flow.filter

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val propertyDetailUiMapper: PropertyDetailUiMapper,
    private val favoritesRepository: FavoritesRepository,
    private val networkMonitor: NetworkMonitor,
    private val startChatUseCase: com.example.g46_kotlin.features.chat.domain.usecase.StartChatUseCase,
    private val analyticsRepository: AnalyticsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val propertyId: String = checkNotNull(savedStateHandle["propertyId"]) {
        "propertyId is required in navigation arguments"
    }

    private val _uiState = MutableStateFlow(PropertyDetailUiState(isLoading = true))
    val uiState: StateFlow<PropertyDetailUiState> = _uiState.asStateFlow()

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { getPropertyByIdUseCase(propertyId) }
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(isLoading = false, detail = propertyDetailUiMapper.toUi(detail))
                    }
                    launch {
                        val rt = analyticsRepository.getLandlordResponseTime(detail.landlordId)
                        _uiState.update { it.copy(responseTimeBucket = rt?.bucket) }
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
        val detail = _uiState.value.detail ?: return
        viewModelScope.launch {
            val card = HousingCardUi(
                id = propertyId,
                name = detail.title,
                pricePerMonth = detail.monthlyRent,
                rating = 0.0,
                neighborhood = detail.neighborhood,
                propertyType = detail.propertyType,
                imageUrl = detail.imageUrl
            )
            favoritesRepository.toggleFavorite(card)
        }
    }

    fun onContactLandlord() {
        val pid = propertyId
        if (_uiState.value.isStartingChat) return
        viewModelScope.launch {
            _uiState.update { it.copy(isStartingChat = true) }
            runCatching { startChatUseCase(pid) }
                .onSuccess { chat ->
                    _uiState.update { it.copy(isStartingChat = false, navigateToChatId = chat.id) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isStartingChat = false, errorMessage = error.message) }
                }
        }
    }

    fun onChatNavigated() {
        _uiState.update { it.copy(navigateToChatId = null) }
    }
}