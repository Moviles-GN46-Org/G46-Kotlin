package com.example.g46_kotlin.features.map.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.g46_kotlin.features.map.presentation.MapMarkerFactory
import com.example.g46_kotlin.features.map.presentation.MapUiState
import com.example.g46_kotlin.features.map.presentation.PropertyPinUi
import com.example.g46_kotlin.features.map.presentation.UserLocationUI
import com.example.g46_kotlin.features.map.presentation.components.DrawerContent
import com.example.g46_kotlin.features.map.presentation.components.DrawerHandleHeader
import com.example.g46_kotlin.features.map.presentation.components.MapSettingsOverlay
import com.example.g46_kotlin.features.map.presentation.components.MiniHouseCard
import com.example.g46_kotlin.features.map.presentation.components.MiniHouseCardUi

@Composable
fun MapScreen(
    uiState: MapUiState,
    onBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onApartmentSelected: (String) -> Unit = {},
    onPropertyClick: (id: String) -> Unit = {}
) {
    var showSettingsOverlay by rememberSaveable { mutableStateOf(false) }

    val onApartmentTapped: (String) -> Unit = { apartmentId ->
        onApartmentSelected(apartmentId)
        onPropertyClick(apartmentId)
    }

    MapScreenLayout(
        uiState = uiState,
        onBack = onBack,
        onSettingsClick = {
            showSettingsOverlay = true
            onSettingsClick()
        },
        onApartmentTapped = onApartmentTapped,
        mapContent = {
            MapRender(
                userLocation = uiState.userLocation,
                apartments = uiState.apartments,
                onApartmentClick = onApartmentTapped
            )
        }
    )

    if (showSettingsOverlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f))
                .clickable { showSettingsOverlay = false },
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.clickable(enabled = false) {}) {
                MapSettingsOverlay(
                    onDismiss = { showSettingsOverlay = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreenLayout(
    uiState: MapUiState,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onApartmentTapped: (String) -> Unit,
    mapContent: @Composable () -> Unit,
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = true
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val isExpanded = sheetState.currentValue == SheetValue.Expanded

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MapTopBar(
                title = stringResource(id = R.string.map_view_title),
                onBack = onBack,
                onSettingsClick = onSettingsClick
            )
        },
        sheetPeekHeight = 90.dp,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetDragHandle = {
            DrawerHandleHeader()
        },
        sheetContent = {
            DrawerContent(
                apartments = uiState.apartments,
                canScroll = isExpanded,
                onApartmentClick = { id ->
                    onApartmentTapped(id)
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            mapContent()

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    modifier = Modifier.align(Alignment.TopCenter),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapTopBar(
    title: String,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.cd_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.cd_settings)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapScreenPreview() {
    val previewState = MapUiState(
        isLoading = false,
        userLocation = UserLocationUI(lat = 4.6016, lon = -74.0661),
        apartments = listOf(
            PropertyPinUi(
                id = "1",
                title = "Oakwood Residences",
                description = "0.5 miles",
                rating = 4.8,
                lat = 4.6020,
                lon = -74.0665,
                price = "$120",
                imageUrl = ""
            ),
            PropertyPinUi(
                id = "2",
                title = "City Lofts",
                description = "0.8 miles",
                rating = 4.6,
                lat = 4.6030,
                lon = -74.0670,
                price = "$450",
                imageUrl = ""
            )
        ),
        selectedApartmentId = null
    )

    G46KotlinTheme {
        MapScreenLayout(
            uiState = previewState,
            onBack = {},
            onSettingsClick = {},
            onApartmentTapped = {},
            mapContent = {
                PreviewMapWithMarkers(
                    prices = listOf("$120", "$450", "$1.200")
                )
            }
        )
    }
}



@Composable
private fun PreviewMapWithMarkers(prices: List<String>) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.map_example),
            contentDescription = "Mapa de ejemplo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Posiciones fijas solo para preview visual
        MarkerBubblePreview(
            price = prices.getOrElse(0) { "$120" },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 70.dp, y = 180.dp)
        )

        MarkerBubblePreview(
            price = prices.getOrElse(1) { "$450" },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 180.dp, y = 280.dp)
        )

        MarkerBubblePreview(
            price = prices.getOrElse(2) { "$1.200" },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 250.dp, y = 220.dp)
        )

        ZoomControlsOverlay(
            onZoomIn = {},
            onZoomOut = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-24).dp)
        )
    }

}

@Composable
private fun MarkerBubblePreview(
    price: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val markerBitmap = remember(price) { MapMarkerFactory.createMarkerBitmap(context, price) }

    Image(
        bitmap = markerBitmap.asImageBitmap(),
        contentDescription = "Marcador con sombra",
        modifier = modifier
    )
}



