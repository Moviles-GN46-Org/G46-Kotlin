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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.g46_kotlin.features.auth.presentation.login.LoginScreen
import com.example.g46_kotlin.features.house.presentation.HouseScreen
import com.example.g46_kotlin.features.map.presentation.MapScreen
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            G46KotlinTheme {
                G46KotlinApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun G46KotlinApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.LOGIN) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (currentDestination == AppDestinations.LOGIN) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                LoginScreen(
                    onLoginSuccess = { currentDestination = AppDestinations.HOME },
                    onSignUpClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Sign up pending")
                        }
                    },
                    onShowMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }
        }
    } else {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries
                    .filter { it != AppDestinations.LOGIN }
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
                when (currentDestination) {
                    AppDestinations.HOME -> {
                        HouseScreen()
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
                        Text("Profile", modifier = Modifier.padding(innerPadding))
                    }

                    AppDestinations.LOGIN -> Unit
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
    HOME("Houses", Icons.Default.Home),
    MAP("Map", Icons.Default.LocationOn),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
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
