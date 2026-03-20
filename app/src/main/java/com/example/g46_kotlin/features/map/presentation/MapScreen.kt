package com.example.g46_kotlin.features.map.presentation

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults


@Composable
private fun MapScreenLayout(
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    mapContent: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            MapTopBar(
                title = stringResource(id = R.string.map_view_title),
                onBack = onBack,
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            mapContent()

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            errorMessage?.let { msg ->
                Text(
                    text = msg,
                    modifier = Modifier.align(Alignment.TopCenter),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@Composable
fun MapScreen(
    onBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val viewModel: MapViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Temporal: dispara carga inicial hasta conectar ubicación real
    LaunchedEffect(Unit) {
        if (uiState.userLocation == null) {
            viewModel.onLocationResolved(lat = 4.7110, lon = -74.0721)
        }
    }

    MapScreenLayout(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onBack = onBack,
        onSettingsClick = onSettingsClick,
        mapContent = {
            Map(
                uiState.userLocation,
                uiState.apartments)
        }
    )
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
    G46KotlinTheme {
        MapScreenLayout(
            isLoading = false,
            errorMessage = null,
            onBack = {},
            onSettingsClick = {},
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


@Composable
private fun ZoomControlsOverlay(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilledTonalButton(onClick = onZoomIn) {
            Text(text = "+")
        }

        FilledTonalButton(onClick = onZoomOut) {
            Text(text = "-")
        }
    }
}

@Composable
fun Map(
    userLocation: UserLocationUI?,
    apartments: List<ApartmentPinUi>
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    var mapRef by remember { mutableStateOf<MapView?>(null)}

    val minZoom = 15.0
    val maxZoom = 25.0
    val zoomStep = 1.0

    var zoomAnimator by remember { mutableStateOf<ValueAnimator?>(null) }


    fun animateZoomBy(delta: Double) {
        val map = mapRef ?: return
        val start = map.zoomLevelDouble
        val end = (start + delta).coerceIn(minZoom, maxZoom)
        if (start == end) return

        zoomAnimator?.cancel()
        zoomAnimator = ValueAnimator.ofFloat(start.toFloat(), end.toFloat()).apply {
            duration = 220L
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                val z = (animator.animatedValue as Float).toDouble()
                map.controller.setZoom(z)
            }
            start()
        }
    }

    DisposableEffect(Unit) {
        onDispose { zoomAnimator?.cancel() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(19.0)

                    minZoomLevel = minZoom
                    maxZoomLevel = maxZoom

                    zoomController.setVisibility(
                        CustomZoomButtonsController.Visibility.NEVER
                    )

                    mapRef = this
                }
            },
            update = { mapView ->
                mapRef = mapView
                mapView.overlays.clear()

                if (!initialized && userLocation != null) {
                    val userPoint = GeoPoint(userLocation.lat, userLocation.lon)
                    mapView.controller.setCenter(userPoint)
                    initialized = true
                }

                apartments.forEach { apt ->
                    val marker = Marker(mapView).apply {
                        position = GeoPoint(apt.lat, apt.lon)
                        title = apt.title
                        snippet = "${apt.description} · ${apt.rating}"
                        icon = MapMarkerFactory.createMarkerIcon(context, apt.price)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)
                }

                mapView.invalidate()
            }
        )

        ZoomControlsOverlay(
            onZoomIn = { animateZoomBy(+zoomStep) },
            onZoomOut = { animateZoomBy(-zoomStep) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-24).dp)
        )

    }
}