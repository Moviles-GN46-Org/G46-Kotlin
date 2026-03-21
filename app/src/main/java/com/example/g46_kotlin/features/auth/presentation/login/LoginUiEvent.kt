package com.example.g46_kotlin.features.auth.presentation.login


sealed interface LoginUiEvent {
    data class OnEmailChanged(val value: String) : LoginUiEvent
    data class OnPasswordChanged(val value: String) : LoginUiEvent
    data class OnRememberMeChanged(val value: Boolean) : LoginUiEvent

    data object OnSubmit : LoginUiEvent
    data object OnForgotPasswordClick : LoginUiEvent
    data object OnGoogleClick : LoginUiEvent
    data object OnAppleClick : LoginUiEvent
    data object OnSignUpClick : LoginUiEvent
}