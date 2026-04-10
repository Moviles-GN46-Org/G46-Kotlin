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
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

@Composable
fun AverageRentCard(avgRent: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Average rent of the zone:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = formatCopCompact(avgRent),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun formatCopCompact(value: Int): String {
    val grouped = value.toString().reversed().chunked(3).joinToString("'").reversed()
    return "$$grouped"
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun AverageRentCardPreview() {
    G46KotlinTheme() {
        AverageRentCard(avgRent = 1000000, modifier = Modifier)
    }
}