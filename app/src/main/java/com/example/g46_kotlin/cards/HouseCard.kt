package com.example.g46_kotlin.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.ui.theme.G46KotlinTheme


data class HousingCardUi(
    val id: String,
    val name: String,
    val pricePerMonth: Int,
    val rating: Double,
    val neighborhood: String,
    val propertyType: String,
)

// Todo: put the font with the general theme
// use the library for the icons insted of my web

@Composable
fun HousingCard(
    ui: HousingCardUi,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onAvailabilityClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onCardClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(ui.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Chip("☆ ${ui.rating}")
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column{
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "\uD83D\uDCCD ${ui.neighborhood}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "\uD83C\uDFE0 ${ui.propertyType}",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(14.dp))
                }

                Button(
                    onClick = onAvailabilityClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("$${ui.pricePerMonth}/mo")
                }
            }
        }
    }
}

@Composable
private fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun HousingCardPreview() {
    G46KotlinTheme(dynamicColor = false) {
        HousingCard(
            ui = HousingCardUi(
                id = "p1",
                name = "Oakwood Residences",
                pricePerMonth = 850,
                rating = 4.8,
                neighborhood = "0.5 miles",
                propertyType = "2 Bed · Shared Kitchen",
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}