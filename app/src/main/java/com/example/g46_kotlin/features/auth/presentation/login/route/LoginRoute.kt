package com.example.g46_kotlin.features.auth.presentation.login.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.auth.presentation.login.LoginEffect
import com.example.g46_kotlin.features.auth.presentation.login.screen.LoginScreen
import com.example.g46_kotlin.features.auth.presentation.login.LoginViewModel

@Composable
fun LoginRoute(
    onSignUpClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToSignUp -> onSignUpClick()
                is LoginEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}