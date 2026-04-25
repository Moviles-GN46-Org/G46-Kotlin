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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = WarmWhite,

    primaryContainer = DeepMocha,
    onPrimaryContainer = WarmWhite,

    secondary = DustyTaupe,
    onSecondary = WarmWhite,

    secondaryContainer = DustyTaupe,
    onSecondaryContainer = WarmWhite,

    background = DarkBackground,
    onBackground = WarmWhite,

    surface = DarkSurface,
    onSurface = WarmWhite,

    surfaceVariant = Color(0xFF4A3A31),
    onSurfaceVariant = WarmWhite
)

private val LightColorScheme = lightColorScheme(
    primary = LightBronze,
    onPrimary = WarmWhite,

    primaryContainer = LightBronze.copy(alpha = 0.22f),
    onPrimaryContainer = DeepMocha,

    secondary = DustyTaupe,
    onSecondary = WarmWhite,

    secondaryContainer = LightBronze.copy(alpha = 0.22f),
    onSecondaryContainer = DeepMocha,

    background = Linen,
    onBackground = AshBrown,

    surface = Linen,
    onSurface = AshBrown,

    surfaceVariant = WarmLinen,
    onSurfaceVariant = DustyTaupe,

    tertiary = DeepMocha
)


@Composable
fun G46KotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}