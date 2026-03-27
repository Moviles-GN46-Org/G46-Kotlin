package com.example.g46_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.features.auth.presentation.login.LoginScreen
import com.example.g46_kotlin.features.house.presentation.HouseScreen
import com.example.g46_kotlin.features.map.presentation.MapScreen
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.auth.presentation.session.AppSplashScreen
import com.example.g46_kotlin.features.auth.presentation.session.SessionUiState
import com.example.g46_kotlin.features.auth.presentation.session.SessionViewModel
import kotlinx.coroutines.launch
import com.example.g46_kotlin.features.auth.presentation.signup.SignupScreen
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.g46_kotlin.navigation.AppRoutes
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.g46_kotlin.ui.theme.WarmLinen
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.ui.theme.DustyTaupe
import com.example.g46_kotlin.ui.theme.LightBronze


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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    if (sessionState is SessionUiState.Loading) {
        AppSplashScreen()
        return
    }

    // Navigation for unauthenticated users
    if (sessionState is SessionUiState.Unauthenticated) {

        NavHost(
            navController = navController,
            startDestination = AppRoutes.Login
        ) {
            composable(AppRoutes.Login) {
                LoginScreen(
                    onLoginSuccess = {
                        sessionViewModel.checkSession()
                    },
                    onSignUpClick = { navController.navigate(AppRoutes.Signup) },
                    onShowMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }

            composable(AppRoutes.Signup) {
                SignupScreen(
                    onBackClick = { navController.popBackStack() },
                    onSignupFinished = { navController.popBackStack() },
                    onShowMessage = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }
        }
        return
    }

    val navItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = LightBronze,
            unselectedIconColor = DustyTaupe,
            selectedTextColor = LightBronze,
            unselectedTextColor = DustyTaupe
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = LightBronze,
            unselectedIconColor = DustyTaupe,
            selectedTextColor = LightBronze,
            unselectedTextColor = DustyTaupe
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0,0,0,0),
        bottomBar = {
            CasandesBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (sessionState is SessionUiState.Authenticated) AppRoutes.Home else AppRoutes.Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.Home) {
                HouseScreen(
                    onMapClick = {
                        navController.navigate(AppRoutes.Map) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AppRoutes.Map) {
                MapScreen(
                    onBack = { navController.popBackStack() },
                    onPropertyClick = {
                            id -> navController.navigate(AppRoutes.propertyDetail(id)) {
                        launchSingleTop = true
                    }
                    }
                )
            }
            //TODO: Implementar view de profile mas adelante, quitar logout
            composable(AppRoutes.Profile) {
                Button(onClick = {
                    sessionViewModel.logout()
                }) { Text("Log out") }
            }

            //TODO: Implementar vista de chats
            composable(AppRoutes.Chats){
                Text(text = "Chats")
            }

            //TODO: Implementar vista de feed
            composable(AppRoutes.Feed){
                Text(text = "Feed")
            }

            //TODO: Implementar vista de Roomies
            composable(AppRoutes.Roomies){
                Text(text = "Roomies")
            }

            composable(
                route = AppRoutes.PropertyDetail,
                arguments = listOf(navArgument("propertyId") {type = NavType.StringType})
            ) { backStackEntry ->
                @Suppress("UNUSED_VARIABLE")
                val propertyId = backStackEntry.arguments?.getString("propertyId")
                // TODO: Implementar view para que funcione solamente con el id
                // PropertyDetailScreen(propertyId = propertyId)
            }
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun AuthenticatedShellPreviewContent(
    currentRoute: String = AppRoutes.Home,
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            CasandesBottomBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Home")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeWithBottomBarPreview() {
    G46KotlinTheme {
        AuthenticatedShellPreviewContent(currentRoute = AppRoutes.Home)
    }
}

@Composable
private fun CasandesBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val barShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = barShape,
                clip = false
            )
            .clip(barShape)
            .background(WarmLinen)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 34.dp, top = 8.dp, end = 34.dp, bottom = 4.dp),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            val items = listOf(
                Triple(AppRoutes.Home, R.drawable.ic_house, "Home"),
                Triple(AppRoutes.Chats, R.drawable.ic_messages_square, "Chats"),
                Triple(AppRoutes.Feed, R.drawable.ic_images, "Feed"),
                Triple(AppRoutes.Roomies, R.drawable.ic_heart_handshake, "Roomies"),
                Triple(AppRoutes.Profile, R.drawable.ic_circle_user_round, "Profile")
            )

            items.forEach { (route, iconRes, labelText) ->
                val selected = currentRoute == route
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(route) },
                    icon = {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = labelText,
                            modifier = Modifier.size(35.dp),
                            tint = if (selected) LightBronze else DustyTaupe
                        )
                    },
                    label = {
                        Text(
                            text = labelText,
                            color = if (selected) LightBronze else DustyTaupe
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
