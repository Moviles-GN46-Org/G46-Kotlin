package com.example.g46_kotlin.features.house.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.cards.HousingCard
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.features.house.presentation.HouseUiState
import com.example.g46_kotlin.ui.theme.G46KotlinTheme

private val budgetOptions = listOf("<=1.2M", "<=1.8M", "<=2.5M")
private val minBedroomOptions = listOf(1, 2, 3, 4)

@Composable
fun HouseScreen(
    uiState: HouseUiState,
    onNeighborhoodChange: (String) -> Unit,
    onBudgetClick: (String) -> Unit,
    onMinBedroomsClick: (Int) -> Unit,
    onFurnishedToggle: () -> Unit,
    onPetFriendlyToggle: () -> Unit,
    onSortByDistanceToggle: () -> Unit,
    onRadiusKmChange: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onApplyDistanceRecommendation: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onAvailabilityClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onToggleFavorite: (HousingCardUi) -> Unit,
    onFavoritesClick: () -> Unit,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit
) {
    HouseContent(
        state = uiState,
        onNeighborhoodChange = onNeighborhoodChange,
        onBudgetClick = onBudgetClick,
        onMinBedroomsClick = onMinBedroomsClick,
        onFurnishedToggle = onFurnishedToggle,
        onPetFriendlyToggle = onPetFriendlyToggle,
        onSortByDistanceToggle = onSortByDistanceToggle,
        onRadiusKmChange = onRadiusKmChange,
        onSearchSubmitted = onSearchSubmitted,
        onApplyDistanceRecommendation = onApplyDistanceRecommendation,
        onAvailabilityClick = onAvailabilityClick,
        onMapClick = onMapClick,
        onPropertyClick = onPropertyClick,
        onNotificationsClick = onNotificationsClick,
        onFavoritesClick = onFavoritesClick,
        onToggleFavorite = onToggleFavorite,
        onNextPage = onNextPage,
        onPrevPage = onPrevPage
    )
}

@Composable
private fun HouseContent(
    state: HouseUiState,
    onNeighborhoodChange: (String) -> Unit,
    onBudgetClick: (String) -> Unit,
    onMinBedroomsClick: (Int) -> Unit,
    onFurnishedToggle: () -> Unit,
    onPetFriendlyToggle: () -> Unit,
    onSortByDistanceToggle: () -> Unit,
    onRadiusKmChange: (String) -> Unit,
    onSearchSubmitted: () -> Unit,
    onApplyDistanceRecommendation: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onAvailabilityClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onToggleFavorite: (HousingCardUi) -> Unit,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            item {
                HouseHeader(
                    onMapClick = onMapClick,
                    onNotificationsClick = onNotificationsClick,
                    onFavoritesClick = onFavoritesClick
                )
            }
            if (state.globalDistanceInsight != null) {
                val insight = state.globalDistanceInsight
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        tonalElevation = 2.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = insight.message,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Muestras: ${insight.samples}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            } else if (state.globalDistanceInsightFallbackMessage != null && !state.isGlobalDistanceInsightLoading) {
                item {
                    Text(
                        text = state.globalDistanceInsightFallbackMessage,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = state.neighborhoodQuery,
                    onValueChange = onNeighborhoodChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    placeholder = { Text("Neighborhood (e.g. Chapinero)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = state.radiusKmInput,
                    onValueChange = onRadiusKmChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    singleLine = true,
                    placeholder = { Text("Max distance in km (optional)") },
                    shape = MaterialTheme.shapes.large
                )
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(budgetOptions) { option ->
                        FilterChip(
                            selected = state.selectedBudget == option,
                            onClick = { onBudgetClick(option) },
                            label = { Text(option) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    items(minBedroomOptions) { bedroomCount ->
                        FilterChip(
                            selected = state.selectedMinBedrooms == bedroomCount,
                            onClick = { onMinBedroomsClick(bedroomCount) },
                            label = { Text("${bedroomCount}+ beds") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = state.furnished == true,
                            onClick = onFurnishedToggle,
                            label = { Text("Furnished") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = state.petFriendly == true,
                            onClick = onPetFriendlyToggle,
                            label = { Text("Pet friendly") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = state.sortByDistance,
                            onClick = onSortByDistanceToggle,
                            label = { Text("Sort: distance") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = onSearchSubmitted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Buscar")
                }
            }

            state.offlineMessage?.let { msg ->
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = msg,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            if (!state.isLoading && state.errorMessage == null && state.houses.isEmpty()) {
                item {
                    Text(
                        text = "No encontramos propiedades con estos filtros.",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(state.houses) { house ->
                HousingCard(
                    ui = house,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    isFavorite = house.id in state.favoriteIds,
                    onToggleFavorite = { onToggleFavorite(house) },
                    onCardClick = { onPropertyClick(house.id) },
                    onAvailabilityClick = { onAvailabilityClick(house.name) }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val prevEnabled = if (state.isOffline) {
                        state.page > 1 && (state.page - 1) in state.cachedPageNumbers && !state.isLoading
                    } else {
                        state.page > 1 && !state.isLoading
                    }
                    val nextEnabled = if (state.isOffline) {
                        (state.page + 1) in state.cachedPageNumbers && !state.isLoading
                    } else {
                        state.page < state.totalPages && !state.isLoading
                    }

                    Button(onClick = onPrevPage, enabled = prevEnabled) { Text("← Anterior") }
                    Text(
                        text = "Página ${state.page} de ${state.totalPages}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = onNextPage, enabled = nextEnabled) { Text("Siguiente →") }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        state.errorMessage?.let { msg ->
            Text(
                text = msg,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun HouseHeader(
    onMapClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Find your next home",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritos",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onMapClick) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun HouseContentPreview() {
    G46KotlinTheme(dynamicColor = false) {
        HouseContent(
            state = HouseUiState(
                houses = previewHouses
            ),
            onNeighborhoodChange = {},
            onBudgetClick = {},
            onMinBedroomsClick = {},
            onFurnishedToggle = {},
            onPetFriendlyToggle = {},
            onSortByDistanceToggle = {},
            onRadiusKmChange = {},
            onSearchSubmitted = {},
            onApplyDistanceRecommendation = {},
            onPropertyClick = {},
            onAvailabilityClick = {},
            onMapClick = {},
            onNotificationsClick = {},
            onFavoritesClick = {},
            onToggleFavorite = {},
            onNextPage = {},
            onPrevPage = {}
        )
    }
}

private val previewHouses = listOf(
    HousingCardUi(
        id = "p1",
        name = "Lakeside Suite",
        pricePerMonth = 860,
        rating = 4.5,
        neighborhood = "1.0 miles",
        propertyType = "2 Bed · 1 Bath"
    ),
    HousingCardUi(
        id = "p2",
        name = "Riverstone Flat",
        pricePerMonth = 780,
        rating = 4.4,
        neighborhood = "0.6 miles",
        propertyType = "Studio · 1 Bath · Kitchenette"
    ),
    HousingCardUi(
        id = "p3",
        name = "Riverstone Flat2",
        pricePerMonth = 780,
        rating = 4.4,
        neighborhood = "0.6 miles",
        propertyType = "Studio · 1 Bath · Kitchenette"
    )
)

