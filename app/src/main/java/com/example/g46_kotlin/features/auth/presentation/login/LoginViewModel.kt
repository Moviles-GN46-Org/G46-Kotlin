package com.example.g46_kotlin.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.LoginParams
import com.example.g46_kotlin.features.auth.domain.usecase.LoginWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailUseCase: LoginWithEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects: SharedFlow<LoginEffect> = _effects.asSharedFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailError = null,
                        errorMessage = null
                    )
                }
            }

            is LoginUiEvent.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null,
                        errorMessage = null
                    )
                }
            }

            is LoginUiEvent.OnRememberMeChanged -> {
                _uiState.update { it.copy(rememberMe = event.value) }
            }

            LoginUiEvent.OnSubmit -> submitLogin()
            LoginUiEvent.OnSignUpClick -> emitEffect(LoginEffect.NavigateToSignUp)
            LoginUiEvent.OnForgotPasswordClick -> emitEffect(LoginEffect.ShowMessage("Forgot password pending"))
            LoginUiEvent.OnGoogleClick -> emitEffect(LoginEffect.ShowMessage("Google login pending"))
            LoginUiEvent.OnAppleClick -> emitEffect(LoginEffect.ShowMessage("Apple login pending"))
        }
    }

    private fun submitLogin() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        val emailError = when {
            email.isBlank() -> "Email is required"
            !isValidEmail(email) -> "Enter a valid email"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must have at least 6 characters"
            else -> null
        }

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loginWithEmailUseCase(
                LoginParams(
                    email = email,
                    password = password,
                    rememberMe = state.rememberMe
                )
            )

            when (result) {
                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loggedUser = result.user
                        )
                    }
                    _effects.emit(LoginEffect.NavigateToHome)
                }

                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private fun isValidEmail(value: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(value)
    }
}