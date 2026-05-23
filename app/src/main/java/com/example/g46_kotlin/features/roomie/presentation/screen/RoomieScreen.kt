package com.example.g46_kotlin.features.roomie.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.roomie.domain.model.PreferenceCategory
import com.example.g46_kotlin.features.roomie.presentation.RoomieUiEvent
import com.example.g46_kotlin.features.roomie.presentation.RoomieUiState
import com.example.g46_kotlin.features.roomie.presentation.components.LoadingRoomies
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieActions
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCard
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCardUi
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieEmptyState
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieErrorState
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieHeader
import com.example.g46_kotlin.features.roomie.presentation.model.PreferenceRegistry
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun RoomieScreen(
    uiState: RoomieUiState,
    onEvent: (RoomieUiEvent) -> Unit,
    onNotifClick: () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        RoomieHeader(onNotifClick)

        if (uiState.isLoading) {
            LoadingRoomies()
        } else if  (uiState.errorMessage != null) {
            RoomieErrorState(
                errorMessage = uiState.errorMessage,
                onRetry = { onEvent(RoomieUiEvent.OnRetryAfterError) }
            )
        } else if (uiState.current == null) {
            RoomieEmptyState()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            uiState.current?.let {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        RoomieCard(
                            ui = it,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        RoomieActions(
                            uiState = uiState,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }
    }
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, showSystemUi = true)
@Composable
fun RoomieScreenPreview() {

    val testUiState = RoomieUiState(
        current = RoomieCardUi(
            name = "Marcus",
            age = 23,
            matchRate = 72,
            budget = 500,
            job = "Barista & Part-Time Student",
            university = "Javeriana University",
            about = "Creative night owl working café shifts. Easygoing, loves music, and hoping to live with someone open-minded and communicative. \uD83D\uDE0C",
            habitsPreferences = listOfNotNull(
                PreferenceRegistry.find(PreferenceCategory.SLEEP_SCHEDULE, "NIGHT_OWL"),
                PreferenceRegistry.find(PreferenceCategory.CLEANLINESS_LEVEL, "MODERATE"),
                PreferenceRegistry.find(PreferenceCategory.NOISE_PREFERENCE, "QUIET")
            )
        )
    )

    G46KotlinTheme{
        RoomieScreen(
            uiState = testUiState,
            onEvent = {}
        )
    }
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, showSystemUi = true)
@Composable
fun RoomieScreenEmptyPreview() {
    G46KotlinTheme{
        RoomieScreen(
            uiState = RoomieUiState(),
            onEvent = {}
        )
    }
}