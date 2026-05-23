package com.example.g46_kotlin.features.roomie.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import com.example.g46_kotlin.features.roomie.domain.model.PreferenceCategory
import com.example.g46_kotlin.features.roomie.presentation.model.PreferenceRegistry
import com.example.g46_kotlin.features.roomie.presentation.model.PreferenceUiSpec
import coil.compose.AsyncImage

data class RoomieCardUi(
    val id: String = "",
    val name: String,
    val age: Int,
    val matchRate: Int,
    val budget: Int,
    val job: String,
    val university: String,
    val about: String,
    val habitsPreferences: List<PreferenceUiSpec>,
    val profilePicture: String? = null
)

@Composable
fun RoomieCard(
    ui: RoomieCardUi,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onCardClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp)
    ) {

        val imageHeight = 320.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            if (ui.profilePicture.isNullOrBlank()) {
                Image(
                    painter = painterResource(id = R.drawable.im_example_roomie),
                    contentDescription = "Roomie preview image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = ui.profilePicture,
                    contentDescription = "Roomie preview image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.im_example_roomie),
                    error = painterResource(id = R.drawable.im_example_roomie)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp) // prueba 56-72
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
                    .padding(horizontal = 15.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${ui.name}, ${ui.age}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                MatchChip(text = "${ui.matchRate}% MATCH")
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-12).dp),
            shape = RectangleShape,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_briefcase),
                                contentDescription = "Job",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = ui.job,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_graduation_cap),
                                contentDescription = "University",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = ui.university,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${ui.budget}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        )
                        Text(text = "budget", color = MaterialTheme.colorScheme.primary, fontSize = 11.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = "Habits & Preferences", fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)

                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ui.habitsPreferences.take(3).forEach { habit ->
                        PreferenceChip(
                            text = habit.label,
                            iconRes = habit.iconRes
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = "About", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = ui.about,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        platformStyle = PlatformTextStyle(),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Proportional,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                )

            }
        }
    }
}

@Composable
private fun MatchChip(text: String) {
    Box(
        modifier = Modifier
            .height(26.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 13.sp,
            lineHeight = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun PreferenceChip(
    text: String,
    @DrawableRes iconRes: Int
) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(12.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 10.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun RoomieCardPreview() {
    G46KotlinTheme(dynamicColor = false) {
        RoomieCard(
            ui = RoomieCardUi(
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
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}