package com.example.g46_kotlin.features.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.foundation.Image
import coil.compose.AsyncImage

data class MiniHouseCardUi(
    val name: String,
    val pricePerMonth: String,
    val rating: Double,
    val distanceToCampus: String,
    val propertyType: String,
    val imageUrl: String? = null    
)

@Composable
fun MiniHouseCard(
    ui: MiniHouseCardUi,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.clickable { onCardClick() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(132.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (ui.imageUrl.isNullOrBlank()) {
                        Image(
                            painter = painterResource(id = R.drawable.house_example),
                            contentDescription = "Imagen de ${ui.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        AsyncImage(
                            model = ui.imageUrl,
                            contentDescription = "Imagen de ${ui.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.house_example),
                            placeholder = painterResource(id = R.drawable.house_example)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ui.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "★ ${ui.rating}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "\uD83D\uDCCD ${ui.distanceToCampus} from campus",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "\uD83C\uDFE0 ${ui.propertyType}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        PriceTag(
            price = ui.pricePerMonth,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-10).dp, y = (-10).dp)
        )
    }
}

@Composable
private fun PriceTag(
    price: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 7.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = price,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "/mo",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun MiniHouseCardPreview() {
    G46KotlinTheme(dynamicColor = false) {
        MiniHouseCard(
            ui = MiniHouseCardUi(
                name = "Oakwood Residences",
                pricePerMonth = "$1'000",
                rating = 4.8,
                distanceToCampus = "0.5 miles",
                propertyType = "2 Bed · Shared Kitchen"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}