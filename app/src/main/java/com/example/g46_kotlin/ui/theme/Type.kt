package com.example.g46_kotlin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R

val InstrumentSans = FontFamily(
    Font(R.font.instrument_sans_regular, FontWeight.Normal),
    Font(R.font.instrument_sans_medium, FontWeight.Medium),
    Font(R.font.instrument_sans_bold, FontWeight.Bold)
)

private val BaseTypography = Typography()

val Typography = BaseTypography.copy(
    headlineSmall = BaseTypography.headlineSmall.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = BaseTypography.titleLarge.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = BaseTypography.titleMedium.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyLarge = BaseTypography.bodyLarge.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = BaseTypography.bodyMedium.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = BaseTypography.bodySmall.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = BaseTypography.labelSmall.copy(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)
