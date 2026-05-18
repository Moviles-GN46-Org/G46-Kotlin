package com.example.g46_kotlin.features.roomie.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.roomie.domain.model.PreferenceCategory
import com.example.g46_kotlin.features.roomie.presentation.RoomieUiEvent
import com.example.g46_kotlin.features.roomie.presentation.RoomieUiState
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCard
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCardUi
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            uiState.current?.let {
                RoomieCard(
                    ui = it,
                    modifier = Modifier.fillMaxWidth()
                )
            } ?: run {
                Text(
                    text = "No more roomies to show",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun RoomieHeader(
    onNotifClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNotifClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notif),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Find your ideal roomie",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview (showBackground = true, backgroundColor = 0xFFFFFF, showSystemUi = true)
@Composable
fun RoomieScreenPreview() {

    val test_uiState = RoomieUiState(
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
            uiState = test_uiState,
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
