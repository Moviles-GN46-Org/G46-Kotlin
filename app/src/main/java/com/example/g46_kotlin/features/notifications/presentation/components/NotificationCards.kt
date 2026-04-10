package com.example.g46_kotlin.features.notifications.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.notifications.presentation.NotificationCardModel
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun NotificationCardFactory(
    model: NotificationCardModel,
    onReadNotification: (String) -> Unit,
    onPropertyClick: (String) -> Unit,
    onChatClick: (String) -> Unit,
    onRoomieClick: (String) -> Unit
) {
    when (model) {
        is NotificationCardModel.ContextAware -> ContextAwareNotification(
            notificationUi = model,
            onClick = {
                onReadNotification(model.id)
                onPropertyClick(it)
            }
        )
        is NotificationCardModel.Match -> MatchNotification(
            notificationUi = model,
            onClick = {
                onReadNotification(model.id)
                onChatClick(it)
            }
        )
        is NotificationCardModel.Review -> ReviewNotification(
            notificationUi = model,
            onClick = {
                onReadNotification(model.id)
                onPropertyClick(it)
            }
        )
        is NotificationCardModel.Generic -> GenericNotification(
            notificationUi = model,
            onClick = {
                onReadNotification(model.id)
                onRoomieClick(model.id)
            }
        )
        is NotificationCardModel.PropertyMatch -> PropertyMatchNotification(
            notificationUi = model,
            onClick = {
                onReadNotification(model.id)
                onPropertyClick(model.propertyId)
            }
        )
    }
}

@Composable
fun PropertyMatchNotification(
    onClick: (String) -> Unit,
    notificationUi: NotificationCardModel.PropertyMatch
) {
    val rentText = "${formatCopThousands(notificationUi.monthlyRentL)}/mo"
    val enrichedMessage = "${notificationUi.message}\n${notificationUi.neighborhood} · $rentText"

    BaseNotificationCard(
        title = notificationUi.title,
        message = enrichedMessage,
        isRead = notificationUi.isRead,
        onClick = { onClick(notificationUi.propertyId) },
        leading = {
            //TODO: Modificar backend para que mande imagen de la propiedad
            Image(
                painter = painterResource(id = R.drawable.house_example),
                contentDescription = "Property Match",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .alpha(if (notificationUi.isRead) 0.6f else 1f),
                contentScale = ContentScale.Crop
            )
        }
    )
}

private fun formatCopThousands(value: Long): String {
    val thousands = (value / 1000).toString()
    val grouped = thousands.reversed().chunked(3).joinToString("'").reversed()
    return "$$grouped"
}


@Composable
fun ContextAwareNotification(
    onClick: (String) -> Unit,
    notificationUi: NotificationCardModel.ContextAware
) {
    BaseNotificationCard(
        title = notificationUi.title,
        message = notificationUi.message,
        isRead = notificationUi.isRead,
        onClick = { onClick(notificationUi.propertyId) },
        leading = {
            val imageModifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .alpha(if (notificationUi.isRead) 0.6f else 1f)

            if (notificationUi.propertyImage.isNotBlank()) {
                AsyncImage(
                    model = notificationUi.propertyImage,
                    contentDescription = "Property",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.house_example),
                    error = painterResource(id = R.drawable.house_example)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.house_example),
                    contentDescription = "Property",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            }
        }

    )
}

//TODO: Modificar para utilizar una foto o un icono distinto
@Composable
fun MatchNotification(
    onClick: (String) -> Unit,
    notificationUi: NotificationCardModel.Match
) {
    BaseNotificationCard(
        title = notificationUi.title,
        message = notificationUi.message,
        isRead = notificationUi.isRead,
        onClick = { onClick(notificationUi.chatId) },
        leading = {
            LeadingIconBox(isRead = notificationUi.isRead) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = "Match",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun ReviewNotification(
    onClick: (String) -> Unit,
    notificationUi: NotificationCardModel.Review
) {
    BaseNotificationCard(
        title = notificationUi.title,
        message = notificationUi.message,
        isRead = notificationUi.isRead,
        onClick = { onClick(notificationUi.propertyId) },
        leading = {
            LeadingIconBox(isRead = notificationUi.isRead) {
                Icon(
                    imageVector = Icons.Outlined.RateReview,
                    contentDescription = "Review",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun GenericNotification(
    onClick: () -> Unit,
    notificationUi: NotificationCardModel.Generic
) {
    BaseNotificationCard(
        title = notificationUi.title,
        message = notificationUi.message,
        isRead = notificationUi.isRead,
        onClick = onClick,
        leading = {
            LeadingIconBox(isRead = notificationUi.isRead) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notification",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun BaseNotificationCard(
    title: String,
    message: String,
    isRead: Boolean,
    onClick: () -> Unit,
    leading: @Composable () -> Unit
) {
    val containerColor = if (isRead) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                leading()

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isRead) FontWeight.Medium else FontWeight.Bold
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (!isRead) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(999.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "New",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun LeadingIconBox(
    isRead: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .alpha(if (isRead) 0.6f else 1f),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "ContextAware Unread")
@Composable
private fun ContextAwareUnreadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        ContextAwareNotification(
            onClick = {},
            notificationUi = NotificationCardModel.ContextAware(
                id = "ctx_u",
                title = "Property near you",
                message = "Lakeside Suite is 450 m away.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = false,
                propertyId = "p_1",
                propertyImage = ""
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "ContextAware Read")
@Composable
private fun ContextAwareReadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        ContextAwareNotification(
            onClick = {},
            notificationUi = NotificationCardModel.ContextAware(
                id = "ctx_r",
                title = "Property near you",
                message = "Lakeside Suite is 450 m away.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = true,
                propertyId = "p_1",
                propertyImage = ""
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Match Unread")
@Composable
private fun MatchUnreadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        MatchNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Match(
                id = "m_u",
                title = "New roommate match",
                message = "You matched with Andrea. Start chatting now.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = false,
                chatId = "chat_1"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Match Read")
@Composable
private fun MatchReadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        MatchNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Match(
                id = "m_r",
                title = "New roommate match",
                message = "You matched with Andrea. Start chatting now.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = true,
                chatId = "chat_1"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Review Unread")
@Composable
private fun ReviewUnreadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        ReviewNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Review(
                id = "r_u",
                title = "New review",
                message = "Your property received a new review.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = false,
                propertyId = "p_2"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Review Read")
@Composable
private fun ReviewReadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        ReviewNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Review(
                id = "r_r",
                title = "New review",
                message = "Your property received a new review.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = true,
                propertyId = "p_2"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Generic Unread")
@Composable
private fun GenericUnreadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        GenericNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Generic(
                id = "g_u",
                title = "System update",
                message = "Your settings were updated.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = false
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Generic Read")
@Composable
private fun GenericReadPreview() {
    G46KotlinTheme(dynamicColor = false) {
        GenericNotification(
            onClick = {},
            notificationUi = NotificationCardModel.Generic(
                id = "g_r",
                title = "System update",
                message = "Your settings were updated.",
                createdAtMillis = System.currentTimeMillis(),
                isRead = true
            )
        )
    }
}
