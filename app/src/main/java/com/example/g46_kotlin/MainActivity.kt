package com.example.g46_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.features.auth.presentation.login.LoginScreen
import com.example.g46_kotlin.features.house.presentation.HouseScreen
import com.example.g46_kotlin.features.map.presentation.MapScreen
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.auth.presentation.session.AppSplashScreen
import com.example.g46_kotlin.features.auth.presentation.session.SessionUiState
import com.example.g46_kotlin.features.auth.presentation.session.SessionViewModel
import kotlinx.coroutines.launch
import com.example.g46_kotlin.features.auth.presentation.signup.SignupScreen
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import kotlin.div
import kotlin.unaryMinus


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            sessionViewModel.uiState.value is SessionUiState.Loading
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            G46KotlinTheme {
                G46KotlinApp(sessionViewModel = sessionViewModel)
            }
        }
    }
}

@Composable
fun G46KotlinApp(
    sessionViewModel: SessionViewModel
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.LOGIN) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

    if (sessionState is SessionUiState.Loading) {
        AppSplashScreen()
        return
    }

    val destinationToRender = when (sessionState) {
        SessionUiState.Authenticated -> {
            if (currentDestination == AppDestinations.LOGIN || currentDestination == AppDestinations.SIGNUP) {
                AppDestinations.HOME
            } else {
                currentDestination
            }
        }
        SessionUiState.Unauthenticated -> {
            if (currentDestination != AppDestinations.LOGIN && currentDestination != AppDestinations.SIGNUP) {
                AppDestinations.LOGIN
            } else {
                currentDestination
            }
        }
        SessionUiState.Loading -> currentDestination
    }

    val appPhase = if (
        destinationToRender == AppDestinations.LOGIN ||
        destinationToRender == AppDestinations.SIGNUP
    ) "AUTH" else "APP"


    LaunchedEffect(destinationToRender) {
        if (currentDestination != destinationToRender) {
            currentDestination = destinationToRender
        }
    }

    AnimatedContent(
        targetState = appPhase,
        transitionSpec = {
            (fadeIn() + slideInVertically { it / 12 })
                .togetherWith(fadeOut() + slideOutVertically { -it / 12 })
        },
        label = "AuthAppTransition"
    ) { phase ->
        if (phase == "AUTH") {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    when (destinationToRender) {
                        AppDestinations.LOGIN -> {
                            LoginScreen(
                                onLoginSuccess = {
                                    sessionViewModel.checkSession()
                                    currentDestination = AppDestinations.HOME
                                },
                                onSignUpClick = { currentDestination = AppDestinations.SIGNUP },
                                onShowMessage = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }

                        AppDestinations.SIGNUP -> {
                            SignupScreen(
                                onBackClick = { currentDestination = AppDestinations.LOGIN },
                                onSignupFinished = { currentDestination = AppDestinations.LOGIN },
                                onShowMessage = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }

                        else -> Unit
                    }
                }
            }
        } else {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    AppDestinations.entries
                        .filter { it != AppDestinations.LOGIN && it != AppDestinations.SIGNUP }
                        .forEach {
                            item(
                                icon = {
                                    Icon(
                                        it.icon,
                                        contentDescription = it.label
                                    )
                                },
                                label = { Text(it.label) },
                                selected = it == currentDestination,
                                onClick = { currentDestination = it }
                            )
                        }
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    when (destinationToRender) {
                        AppDestinations.HOME -> {
                            HouseScreen(
                                onMapClick = { currentDestination = AppDestinations.MAP }
                            )
                        }

                        AppDestinations.MAP -> {
                            MapScreen(
                                onBack = { currentDestination = AppDestinations.HOME },
                            )
                        }

                        AppDestinations.FAVORITES -> {
                            Text("Favorites", modifier = Modifier.padding(innerPadding))
                        }

                        AppDestinations.PROFILE -> {
                            Button(
                                onClick = { sessionViewModel.logout() },
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                Text("Log out")
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    LOGIN("Login", Icons.Default.AccountBox),
    SIGNUP("Sign up", Icons.Default.AccountBox),
    HOME("Houses", Icons.Default.Home),
    MAP("Map", Icons.Default.LocationOn),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola $name.",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    G46KotlinTheme {
        Greeting("G46")
    }
}
