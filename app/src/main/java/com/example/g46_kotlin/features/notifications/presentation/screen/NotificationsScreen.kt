package com.example.g46_kotlin.features.notifications.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.g46_kotlin.features.notifications.presentation.NotificationCardModel
import com.example.g46_kotlin.features.notifications.presentation.NotificationsUiState
import com.example.g46_kotlin.features.notifications.presentation.components.NotificationCardFactory
import com.example.g46_kotlin.features.notifications.presentation.components.NotificationsErrorScreen
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun NotificationsScreen(
    uiState: NotificationsUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onReadNotification: (String) -> Unit = {},
    onReadAll: () -> Unit = {},
    onPropertyClick: (String) -> Unit = {},
    onChatClick: (String) -> Unit,
    onRoomieClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        NotificationsHeader(onBackClick = onBackClick)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                uiState.isLoading -> {
                    NotificationsLoadingState()
                }

                uiState.errorMessage!= null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        NotificationsErrorScreen(
                            onRetryClick = onRetryClick
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        if (uiState.notifications.isEmpty()){
                            item {
                                Text(
                                    text = "No notifications yet",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            items(
                                items = uiState.notifications,
                                key = { it.id }
                            ) { notification ->
                                NotificationCardFactory(
                                    model = notification,
                                    onReadNotification = onReadNotification,
                                    onPropertyClick = onPropertyClick,
                                    onChatClick = onChatClick,
                                    onRoomieClick = onRoomieClick
                                )
                            }
                        }
                    }
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

@Composable
private fun NotificationsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun NotificationsScreenPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(),
            onBackClick = {},
            onRetryClick = {},
            onReadNotification = {},
            onReadAll = {},
            onPropertyClick = {},
            onChatClick = {},
            onRoomieClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun NotificationsErrorPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(
                errorMessage = "Something went wrong"
            ),
            onBackClick = {},
            onRetryClick = {},
            onReadNotification = {},
            onReadAll = {},
            onPropertyClick = {},
            onChatClick = {},
            onRoomieClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun NotificationsLoadingPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(
                isLoading = true
            ),
            onBackClick = {},
            onRetryClick = {},
            onReadNotification = {},
            onReadAll = {},
            onPropertyClick = {},
            onChatClick = {},
            onRoomieClick = {},
        )
    }
}

private val previewNotifications = listOf(
    NotificationCardModel.ContextAware(
        id = "ctx_1",
        title = "Property near you",
        message = "Lakeside Suite is 450 m away.",
        createdAtMillis = System.currentTimeMillis(),
        isRead = false,
        propertyId = "p_101",
        propertyImage = ""
    ),
    NotificationCardModel.Match(
        id = "match_1",
        title = "New roommate match",
        message = "You matched with Andrea. Start chatting now.",
        createdAtMillis = System.currentTimeMillis() - 60_000,
        isRead = false,
        chatId = "chat_22"
    ),
    NotificationCardModel.Review(
        id = "review_1",
        title = "New review",
        message = "Your property received a new review.",
        createdAtMillis = System.currentTimeMillis() - 120_000,
        isRead = true,
        propertyId = "p_303"
    ),
    NotificationCardModel.Generic(
        id = "generic_1",
        title = "System update",
        message = "Your notification settings were updated.",
        createdAtMillis = System.currentTimeMillis() - 180_000,
        isRead = true
    )
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Notifications List Preview")
@Composable
private fun NotificationsListPreview() {
    G46KotlinTheme(dynamicColor = false) {
        NotificationsScreen(
            uiState = NotificationsUiState(
                notifications = previewNotifications,
                unreadCount = previewNotifications.count { !it.isRead }
            ),
            onBackClick = {},
            onRetryClick = {},
            onReadNotification = {},
            onReadAll = {},
            onPropertyClick = {},
            onChatClick = {},
            onRoomieClick = {}
        )
    }
}
