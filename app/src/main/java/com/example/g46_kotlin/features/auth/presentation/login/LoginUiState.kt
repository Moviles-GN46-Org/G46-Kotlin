package com.example.g46_kotlin.features.auth.presentation.login

import com.example.g46_kotlin.features.auth.domain.model.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null,
    val loggedUser: User? = null
)