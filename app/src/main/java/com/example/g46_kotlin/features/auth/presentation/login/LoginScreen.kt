package com.example.g46_kotlin.features.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> onLoginSuccess()
                LoginEffect.NavigateToSignUp -> onSignUpClick()
                is LoginEffect.ShowMessage -> onShowMessage(effect.message)
            }
        }
    }
    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Log in",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(LoginUiEvent.OnEmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    enabled = !uiState.isLoading,
                    singleLine = true,
                    isError = uiState.emailError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                if (uiState.emailError != null) {
                    Text(
                        text = uiState.emailError.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(LoginUiEvent.OnPasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    enabled = !uiState.isLoading,
                    singleLine = true,
                    isError = uiState.passwordError != null,
                    visualTransformation = PasswordVisualTransformation()
                )

                if (uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = {
                                onEvent(LoginUiEvent.OnRememberMeChanged(it))
                            },
                            enabled = !uiState.isLoading
                        )
                        Text(
                            text = "Remember me",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                            onEvent(LoginUiEvent.OnForgotPasswordClick)
                        }
                    )
                }

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(LoginUiEvent.OnSubmit) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.height(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Log in")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEvent(LoginUiEvent.OnGoogleClick) },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    ) {
                        Text("Google")
                    }

                    OutlinedButton(
                        onClick = { onEvent(LoginUiEvent.OnAppleClick) },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    ) {
                        Text("Apple")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don’t have an account? Sign up",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !uiState.isLoading) {
                            onEvent(LoginUiEvent.OnSignUpClick)
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login - Default")
@Composable
private fun LoginScreenPreviewDefault() {
    G46KotlinTheme {
        LoginContent(
            uiState = LoginUiState(),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - Loading")
@Composable
private fun LoginScreenPreviewLoading() {
    G46KotlinTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "student@casandes.edu",
                password = "123456",
                isLoading = true
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - Error")
@Composable
private fun LoginScreenPreviewError() {
    G46KotlinTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "student@casandes.edu",
                password = "123",
                passwordError = "Password must have at least 6 characters",
                errorMessage = "Invalid email or password"
            ),
            onEvent = {}
        )
    }
}