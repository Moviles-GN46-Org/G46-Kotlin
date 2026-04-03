package com.example.g46_kotlin.features.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.features.map.presentation.PropertyPinUi

@Composable
fun DrawerHandleHeader(title: String = "Recommended for you") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DrawerContent(
    apartments: List<PropertyPinUi>,
    canScroll: Boolean,
    onApartmentClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 520.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = canScroll,
        contentPadding = PaddingValues(top = 20.dp,bottom = 20.dp)
    ) {
        items(apartments, key = { it.id }) { apt ->
            MiniHouseCard(
                ui = MiniHouseCardUi(
                    name = apt.title,
                    pricePerMonth = apt.price,
                    rating = apt.rating,
                    distanceToCampus = apt.description, // cámbialo por distancia real si la tienes
                    propertyType = "Apartment",
                    imageUrl = apt.imageUrl
                ),
                onCardClick = { onApartmentClick(apt.id) }
            )
        }
    }
}