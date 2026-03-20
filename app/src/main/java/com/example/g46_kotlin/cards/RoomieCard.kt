package com.example.g46_kotlin.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

data class RoomieCardUi(
    val name: String,
    val age: Int,
    val matchRate: Int,
    val budget: Int,
    val job: String,
    val university: String,
    val about: String,
    val habitsPreferences: List<String>
)

// Todo: put the font with the general theme
// use the library for the icons insted of my web

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
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "${ui.name}, ${ui.age}", fontSize = 22.sp,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    MatchChip(text = "${ui.matchRate}% MATCH")
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(text = "\uD83D\uDCBC ${ui.job}", color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(text = "\uD83D\uDCD6 ${ui.university}", color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "$${ui.budget}", fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary, fontSize = 30.sp / 1.5f)
                        Text(text = "budget", color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = "Habits & Preferences", fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)

                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ui.habitsPreferences.take(3).forEach { habit ->
                        PreferenceChip(text = habit)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(text = "About", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                Text(text = ui.about, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun MatchChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun PreferenceChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                about = "Creative night owl working café shifts. Easygoing, loves music, and hoping to live with someone open-minded and communicative.",
                habitsPreferences = listOf("NIGHT OWL", "PET LOVER", "LOW NOISE", "EARLY RISER")
            ),
            modifier = Modifier.padding(12.dp)
        )
    }
}