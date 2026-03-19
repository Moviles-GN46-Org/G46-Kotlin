package com.example.g46_kotlin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    primary = LightBronze,
    onPrimary = WarmWhite,

    background = DarkBackground,
    onBackground = WarmWhite,

    surface = DarkSurface,
    onSurface = WarmWhite
)

private val LightColorScheme = lightColorScheme(

    primary = LightBronze,
    onPrimary = WarmWhite,

    secondary = DustyTaupe,
    onSecondary = WarmWhite,

    background = Linen,
    onBackground = AshBrown,

    surface = WarmWhite,
    onSurface = AshBrown,

    tertiary = DeepMocha // puedes usarlo para títulos
)

@Composable
fun G46KotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}