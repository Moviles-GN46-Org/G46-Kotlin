package com.example.g46_kotlin.features.auth.presentation.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import com.example.g46_kotlin.ui.theme.LightBronze

@Composable
fun AppSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBronze),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "Splash logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppSplashScreenPreview() {
    G46KotlinTheme {
        AppSplashScreen()
    }
}
