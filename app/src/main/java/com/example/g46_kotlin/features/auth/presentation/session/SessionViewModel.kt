package com.example.g46_kotlin.features.auth.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.clear

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenStorage: TokenStorage
): ViewModel() {

    private val _uiState = MutableStateFlow<SessionUiState>(SessionUiState.Loading)
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init { checkSession() }

    fun checkSession() {
        viewModelScope.launch {
            tokenStorage.accessTokenFlow.collect { token ->
                _uiState.value = if (token.isNullOrBlank()) {
                    SessionUiState.Unauthenticated
                } else {
                    SessionUiState.Authenticated
                }

            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = SessionUiState.Loading
            tokenStorage.clear()
            delay(300)
        }
    }
}