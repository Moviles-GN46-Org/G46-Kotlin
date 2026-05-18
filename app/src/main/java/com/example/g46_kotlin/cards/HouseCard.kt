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
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.g46_kotlin.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.serialization.Serializable

@Serializable
data class HousingCardUi(
    val id: String,
    val name: String,
    val pricePerMonth: Int,
    val rating: Double,
    val neighborhood: String,
    val propertyType: String,
    val imageUrl: String? = null,
)

// Todo: put the font with the general theme
// use the library for the icons insted of my web

@Composable
fun HousingCard(
    ui: HousingCardUi,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
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
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (ui.imageUrl.isNullOrBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.house_example),
                        contentDescription = "Imagen de ${ui.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    AsyncImage(
                        model = ui.imageUrl,
                        contentDescription = "Imagen de ${ui.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.house_example),
                        placeholder = painterResource(id = R.drawable.house_example)
                    )
                }
                if (onToggleFavorite != null) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) Color(0xFFE53935) else Color.White
                        )
                    }
                }
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