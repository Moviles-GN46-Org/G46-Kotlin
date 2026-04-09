package com.example.g46_kotlin.features.notifications.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.features.notifications.presentation.NotificationsUiState
import com.example.g46_kotlin.features.notifications.presentation.components.NotificationsErrorScreen
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            item {
                NotificationsHeader(
                    onBackClick = onBackClick
                )
            }

            if (uiState.errorMessage != null) {
                item {
                    NotificationsErrorScreen(
                        onRetryClick = onRetryClick
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationsHeader(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
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
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Notifications",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun NotificationsScreenPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(),
            onBackClick = {},
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "NotificationsErrorPreview")
@Composable
private fun NotificationsErrorPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(
                errorMessage = "Something went wrong"
            ),
            onBackClick = {},
            onRetryClick = {}
        )
    }
}