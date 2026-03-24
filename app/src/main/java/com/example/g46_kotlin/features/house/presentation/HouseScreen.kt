package com.example.g46_kotlin.features.house.presentation

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
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.cards.HousingCard
import com.example.g46_kotlin.cards.HousingCardUi
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton


private val budgetOptions = listOf("0-700", "700-1000", "1000-1400", "1400+")
private val roomTypeOptions = listOf("APARTMENT", "ROOM", "STUDIO", "HOUSE", "SHARED ROOM")
private val amenitiesOptions = emptyList<String>()

@Composable
fun HouseScreen(
    onMapClick: () -> Unit = {}
) {
    val viewModel: HouseViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HouseContent(
        state = uiState,
        onQueryChange = viewModel::onQueryChange,
        onBudgetClick = viewModel::onBudgetClick,
        onRoomTypeClick = viewModel::onRoomTypeClick,
        onAmenityClick = viewModel::onAmenityClick,
        onHouseClick = viewModel::onHouseClick,
        onAvailabilityClick = viewModel::onAvailabilityClick,
        onNotificationIconClick = viewModel::onNotificationIconClick,
        onDismissNotificationsPanel = viewModel::onDismissNotificationsPanel,
        onClearNotifications = viewModel::onClearNotifications,
        onBackFromDetail = viewModel::onBackFromDetail,
        onMapClick = onMapClick
    )
}

@Composable
private fun HouseContent(
    state: HouseUiState,
    onQueryChange: (String) -> Unit,
    onBudgetClick: (String) -> Unit,
    onRoomTypeClick: (String) -> Unit,
    onAmenityClick: (String) -> Unit,
    onHouseClick: (String) -> Unit,
    onAvailabilityClick: (String) -> Unit,
    onNotificationIconClick: () -> Unit,
    onDismissNotificationsPanel: () -> Unit,
    onClearNotifications: () -> Unit,
    onBackFromDetail: () -> Unit,
    onMapClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.showPropertyDetail && state.selectedPropertyDetail != null) {
            PropertyDetailScreen(
                detail = state.selectedPropertyDetail,
                onBackClick = onBackFromDetail
            )
        } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    item {
                        HouseHeader(
                            state = state,
                            onNotificationIconClick = onNotificationIconClick,
                            onDismissNotificationsPanel = onDismissNotificationsPanel,
                            onClearNotifications = onClearNotifications,
                            onMapClick = onMapClick
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = onQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            singleLine = true,
                            placeholder = { Text("Search near University...") },
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
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
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

                            items(roomTypeOptions) { option ->
                                FilterChip(
                                    selected = state.selectedRoomType == option,
                                    onClick = { onRoomTypeClick(option) },
                                    label = { Text(option) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }

                            items(amenitiesOptions) { option ->
                                FilterChip(
                                    selected = state.selectedAmenities.contains(option),
                                    onClick = { onAmenityClick(option) },
                                    label = { Text(option) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }
                    }

                    items(state.visibleHouses) { house ->
                        HousingCard(
                            ui = house,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            onCardClick = { onHouseClick(house.id) },
                            onAvailabilityClick = { onAvailabilityClick(house.name) }
                        )
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
}

@Composable
private fun HouseHeader(
    state: HouseUiState,
    onNotificationIconClick: () -> Unit,
    onDismissNotificationsPanel: () -> Unit,
    onClearNotifications: () -> Unit,
    onMapClick: () -> Unit
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

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box {
                IconButton(onClick = onNotificationIconClick) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = state.showNotificationsPanel,
                    onDismissRequest = onDismissNotificationsPanel
                ) {
                    if (state.notifications.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No notifications") },
                            onClick = {}
                        )
                    } else {
                        state.notifications.take(5).forEach { notification ->
                            val prefix = if (notification.isRead) "" else "[new] "
                            DropdownMenuItem(
                                text = { Text(prefix + notification.title) },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text(notification.message) },
                                onClick = {}
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Clear all") },
                            onClick = onClearNotifications
                        )
                    }
                }
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
                houses = previewHouses,
                visibleHouses = previewHouses
            ),
            onQueryChange = {},
            onBudgetClick = {},
            onRoomTypeClick = {},
            onAmenityClick = {},
            onHouseClick = {},
            onAvailabilityClick = {},
            onNotificationIconClick = {},
            onDismissNotificationsPanel = {},
            onClearNotifications = {},
            onBackFromDetail = {},
            onMapClick = {}
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
        propertyType = "2 Bed · 1 Bath",

    ),
    HousingCardUi(
        id = "p2",
        name = "Riverstone Flat",
        pricePerMonth = 780,
        rating = 4.4,
        neighborhood = "0.6 miles",
        propertyType = "Studio · 1 Bath · Kitchenette",
    ),
    HousingCardUi(
        id = "p3",
        name = "Riverstone Flat2",
        pricePerMonth = 780,
        rating = 4.4,
        neighborhood = "0.6 miles",
        propertyType = "Studio · 1 Bath · Kitchenette",
    )
)