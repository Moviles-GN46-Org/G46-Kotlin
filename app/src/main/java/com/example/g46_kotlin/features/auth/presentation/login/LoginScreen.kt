package com.example.g46_kotlin.features.auth.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.example.g46_kotlin.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.ui.theme.DustyTaupe
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.example.g46_kotlin.ui.theme.LightBronze
import com.example.g46_kotlin.ui.theme.WarmLinen


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

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val inputIconTint = DustyTaupe
    val inputIconSize = 18.dp
    val labelSize = 15.sp
    val inputShape = RoundedCornerShape(17.dp)
    val titleSize: TextUnit = 26.sp
    val logoTint = LightBronze
    val subtitleText = "Sign in to connect with your campus housing community"
    val logoHeightDp = with(LocalDensity.current) {titleSize.toDp() + 5.dp }
    val logoBottomNudge = (-1).dp
    val loginButtonShape = RoundedCornerShape(14.dp)


    Box(modifier = Modifier.fillMaxSize()) {
        // ajustar altitud
        val topOffset = 235.dp
        val corners = 25.dp
        val sheetShape = RoundedCornerShape(topStart = corners, topEnd = corners)

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
                .fillMaxHeight()
                .padding(top = topOffset),
            shape = sheetShape,
            colors = CardDefaults.cardColors(
                containerColor = WarmLinen
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        // padding del form
                        start = 24.dp,
                        end = 24.dp,
                        top = 40.dp,
                        bottom = 28.dp
                    )
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Welcome to",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = titleSize,
                                lineHeight = titleSize
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.splash_logo),
                            contentDescription = "Casandes",
                            tint = logoTint,
                            modifier = Modifier
                                .height(logoHeightDp)
                                .wrapContentHeight()
                                .offset(y = logoBottomNudge)
                                .alignBy { it.measuredHeight }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = subtitleText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = labelSize),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Email",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = labelSize),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = inputShape,
                            clip = false
                        )
                ) {
                    OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(LoginUiEvent.OnEmailChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = "janedoe@email.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mail),
                            contentDescription = "Email icon",
                            tint = inputIconTint,
                            modifier = Modifier.size(inputIconSize)
                        )
                    },
                    enabled = !uiState.isLoading,
                    singleLine = true,
                    isError = uiState.emailError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = DustyTaupe,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    )
                )
                }

                if (uiState.emailError != null) {
                    Text(
                        text = uiState.emailError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Password",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = labelSize),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = inputShape,
                            clip = false
                        )
                ) {
                    OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(LoginUiEvent.OnPasswordChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "Lock icon",
                            tint = inputIconTint,
                            modifier = Modifier.size(inputIconSize)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },

                    enabled = !uiState.isLoading,
                    singleLine = true,
                    isError = uiState.passwordError != null,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(17.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = DustyTaupe,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                    )
                )
                }
                if (uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }


                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(17.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White)
                                .border(
                                    width = 1.5.dp,
                                    color = DustyTaupe,
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Checkbox(
                                checked = uiState.rememberMe,
                                onCheckedChange = { onEvent(LoginUiEvent.OnRememberMeChanged(it)) },
                                enabled = !uiState.isLoading,
                                modifier = Modifier.size(20.dp),
                                colors = CheckboxDefaults.colors(
                                    checkedColor = LightBronze,                  // fondo cuando está checked
                                    uncheckedColor = Color.Transparent,          // evita borde/cuadro visible interno
                                    checkmarkColor = Color.White,
                                    disabledCheckedColor = LightBronze.copy(alpha = 0.4f),
                                    disabledUncheckedColor = Color.Transparent,
                                    disabledIndeterminateColor = Color.Transparent
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Remember me",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                            onEvent(LoginUiEvent.OnForgotPasswordClick)
                        }
                    )
                }


                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .blur(2.dp)
                            .background(
                                LightBronze.copy(alpha = 0.3f),
                                shape = loginButtonShape
                            )
                    )

                    Button(
                        onClick = { onEvent(LoginUiEvent.OnSubmit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        enabled = !uiState.isLoading,
                        shape = loginButtonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBronze,
                            contentColor = Color.White
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Log in",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp
                    )

                    Text(
                        text = "or log in with",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .background(WarmLinen)
                            .padding(horizontal = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEvent(LoginUiEvent.OnGoogleClick) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        enabled = !uiState.isLoading,
                        shape = loginButtonShape,
                        border = BorderStroke(1.dp, LightBronze.copy(alpha = 0.45f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LightBronze.copy(alpha = 0.12f),
                            contentColor = LightBronze,
                            disabledContainerColor = LightBronze.copy(alpha = 0.08f),
                            disabledContentColor = LightBronze.copy(alpha = 0.45f)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google",
                            tint = LightBronze,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = LightBronze
                        )
                    }

                    OutlinedButton(
                        onClick = { onEvent(LoginUiEvent.OnAppleClick) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        enabled = !uiState.isLoading,
                        shape = loginButtonShape,
                        border = BorderStroke(1.dp, LightBronze.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LightBronze.copy(alpha = 0.12f),
                            contentColor = LightBronze,
                            disabledContainerColor = LightBronze.copy(alpha = 0.08f),
                            disabledContentColor = LightBronze.copy(alpha = 0.45f)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_apple),
                            contentDescription = "Apple",
                            tint = LightBronze,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Apple",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = LightBronze
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don’t have an account?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                            onEvent(LoginUiEvent.OnSignUpClick)
                        }
                    )
                }
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