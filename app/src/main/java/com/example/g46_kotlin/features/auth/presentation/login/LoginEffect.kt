package com.example.g46_kotlin.features.auth.presentation.login

sealed interface LoginEffect {
    data object NavigateToSignUp : LoginEffect
    data class ShowMessage(val message: String) : LoginEffect
    data class NavigateToNotImplemented(val title: String,val message: String) : LoginEffect
}