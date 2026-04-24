package com.example.g46_kotlin.features.map.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.features.map.domain.model.PopularSize
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun MostPopularAptSizeCard(size: PopularSize, modifier: Modifier){

    val min = size.minM2
    val max = size.maxM2
    val sizeRange = "$min - $max m²"

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Top apartment size near uni:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = sizeRange,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun MostPopularAptSizeCardPreview(){

    val sizes = PopularSize(
        minM2 = 25,
        maxM2 = 30,
        count = 10,
        avgSizeM2 = 28.2
    )

    G46KotlinTheme() {
        MostPopularAptSizeCard(size = sizes, modifier = Modifier)
    }
}
