package com.example.g46_kotlin.features.auth.presentation.login.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.auth.presentation.login.LoginUiEvent
import com.example.g46_kotlin.features.auth.presentation.login.LoginUiState
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginEmailField
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginHeaderSection
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginPasswordField
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginRememberForgotRow
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginSignupFooter
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginSocialSection
import com.example.g46_kotlin.features.auth.presentation.login.components.LoginSubmitButton
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import com.example.g46_kotlin.ui.theme.WarmLinen

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val topOffset = 235.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_image),
            contentDescription = "Login header image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(topOffset + 10.dp)
                .graphicsLayer {
                    scaleX = 1.2f
                    scaleY = 1.2f
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
                .align(Alignment.TopCenter)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topOffset),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 28.dp)
            ) {
                LoginHeaderSection()

                Spacer(modifier = Modifier.height(20.dp))
                LoginEmailField(uiState = uiState, onEvent = onEvent)

                Spacer(modifier = Modifier.height(12.dp))
                LoginPasswordField(
                    uiState = uiState,
                    passwordVisible = passwordVisible,
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    onEvent = onEvent
                )

                Spacer(modifier = Modifier.height(15.dp))
                LoginRememberForgotRow(uiState = uiState, onEvent = onEvent)

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                LoginSubmitButton(uiState = uiState, onEvent = onEvent)

                Spacer(modifier = Modifier.height(15.dp))
                LoginSocialSection(uiState = uiState, onEvent = onEvent)

                Spacer(modifier = Modifier.height(16.dp))
                LoginSignupFooter(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Refactor - Default")
@Composable
private fun LoginPreviewDefault() {
    G46KotlinTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Login Refactor - Loading")
@Composable
private fun LoginPreviewLoading() {
    G46KotlinTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "student@casandes.edu",
                password = "123456",
                isLoading = true
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Login Refactor - Error")
@Composable
private fun LoginPreviewError() {
    G46KotlinTheme {
        LoginScreen(
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

