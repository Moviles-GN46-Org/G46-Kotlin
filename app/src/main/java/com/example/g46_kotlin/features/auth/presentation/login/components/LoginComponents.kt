package com.example.g46_kotlin.features.auth.presentation.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.auth.presentation.login.LoginUiEvent
import com.example.g46_kotlin.features.auth.presentation.login.LoginUiState
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import com.example.g46_kotlin.ui.theme.DustyTaupe
import com.example.g46_kotlin.ui.theme.LightBronze
import com.example.g46_kotlin.ui.theme.WarmLinen

private val inputShape = RoundedCornerShape(17.dp)
private val loginButtonShape = RoundedCornerShape(14.dp)
private val inputIconSize = 18.dp
private val labelSize = 15.sp

@Composable
fun LoginHeaderSection(modifier: Modifier = Modifier) {
    val titleSize = 26.sp
    val logoBottomNudge = (-1).dp

    Column(
        modifier = modifier.fillMaxWidth(),
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
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(31.dp)
                    .wrapContentHeight()
                    .offset(y = logoBottomNudge)
                    .alignBy { it.measuredHeight }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign in to connect with your campus housing community",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = labelSize
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LoginEmailField(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Text(
        text = "Email",
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = labelSize
        ),
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = inputShape, clip = false)
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = "Email icon",
                    tint = DustyTaupe,
                    modifier = Modifier.size(inputIconSize)
                )
            },
            enabled = !uiState.isLoading,
            singleLine = true,
            isError = uiState.emailError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = inputShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
    }

    uiState.emailError?.let { error ->
        Text(
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun LoginPasswordField(
    uiState: LoginUiState,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onEvent: (LoginUiEvent) -> Unit
) {
    Text(
        text = "Password",
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = labelSize
        ),
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = inputShape, clip = false)
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = "Lock icon",
                    tint = DustyTaupe,
                    modifier = Modifier.size(inputIconSize)
                )
            },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
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
            shape = inputShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
    }

    uiState.passwordError?.let { error ->
        Text(
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun LoginRememberForgotRow(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
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
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = Color.Transparent,
                        checkmarkColor = Color.White,
                        disabledCheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
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
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                onEvent(LoginUiEvent.OnForgotPasswordClick)
            }
        )
    }
}

@Composable
fun LoginSubmitButton(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
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
                .background(LightBronze.copy(alpha = 0.3f), shape = loginButtonShape)
        )

        Button(
            onClick = { onEvent(LoginUiEvent.OnSubmit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            enabled = !uiState.isLoading,
            shape = loginButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
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
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun LoginSocialSection(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            thickness = 1.dp
        )

        Text(
            text = "or log in with",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
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
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
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
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_apple),
                contentDescription = "Apple",
                tint = MaterialTheme.colorScheme.primary,
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
}

@Composable
fun LoginSignupFooter(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don’t have an account?",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "Sign up",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                onEvent(LoginUiEvent.OnSignUpClick)
            }
        )
    }
}

@Preview(showBackground = true, name = "Login Components - Header")
@Composable
private fun LoginHeaderSectionPreview() {
    G46KotlinTheme {
        LoginHeaderSection(modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, name = "Login Components - Form Block")
@Composable
private fun LoginFieldsBlockPreview() {
    G46KotlinTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LoginEmailField(uiState = LoginUiState(), onEvent = {})
            Spacer(modifier = Modifier.height(12.dp))
            LoginPasswordField(
                uiState = LoginUiState(),
                passwordVisible = false,
                onTogglePasswordVisibility = {},
                onEvent = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            LoginRememberForgotRow(uiState = LoginUiState(), onEvent = {})
        }
    }
}


