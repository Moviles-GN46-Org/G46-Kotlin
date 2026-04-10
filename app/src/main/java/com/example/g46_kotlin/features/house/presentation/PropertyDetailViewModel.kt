package com.example.g46_kotlin.features.house.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.house.presentation.mapper.PropertyDetailUiMapper
import com.example.g46_kotlin.features.house.domain.usecase.GetPropertyByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getPropertyByIdUseCase: GetPropertyByIdUseCase,
    private val propertyDetailUiMapper: PropertyDetailUiMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val propertyId: String = checkNotNull(savedStateHandle["propertyId"]) {
        "propertyId is required in navigation arguments"
    }

    private val _uiState = MutableStateFlow(PropertyDetailUiState(isLoading = true))
    val uiState: StateFlow<PropertyDetailUiState> = _uiState.asStateFlow()

    init{
        loadProperty()
    }

    fun loadProperty() {
        viewModelScope.launch {
            _uiState.value = PropertyDetailUiState(isLoading = true)

            runCatching { getPropertyByIdUseCase(propertyId) }
                .onSuccess { detail ->
                    _uiState.value = PropertyDetailUiState(
                        isLoading = false,
                        detail = propertyDetailUiMapper.toUi(detail)
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = PropertyDetailUiState(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unknown error"
                    )
                }
        }
    }
}