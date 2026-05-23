package com.example.g46_kotlin.features.roomie.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieHeader
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieOfflineBanner
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun RoomieOfflineScreen(
    onNotifClick: () -> Unit,
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
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            RoomieOfflineBanner()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, showSystemUi = true)
@Composable
fun RoomieOfflineScreenPreview() {
    G46KotlinTheme {
        RoomieOfflineScreen(onNotifClick = {})
    }
}