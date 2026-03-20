package com.example.g46_kotlin.features.map.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.g46_kotlin.R
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.createBitmap
import kotlin.text.clear
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.remember
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import org.osmdroid.views.CustomZoomButtonsController


@Composable
fun MapScreen() {
    val viewModel: MapViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Temporal: dispara carga inicial hasta conectar ubicación real
    LaunchedEffect(Unit) {
        if (uiState.userLocation == null) {
            viewModel.onLocationResolved(lat = 4.7110, lon = -74.0721)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Map(
            userLocation = uiState.userLocation,
            apartments = uiState.apartments
        )

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapScreenPreview() {

    G46KotlinTheme{
        PreviewMapWithMarkers(
            prices = listOf("$120", "$450", "$1.200")
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
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.view_map_marker, null).apply {
                findViewById<TextView>(R.id.tvMarkerPrice).text = price
            }
        },
        update = { view ->
            view.findViewById<TextView>(R.id.tvMarkerPrice).text = price
        }
    )
}

private fun createMarkerIcon(context: Context, price: String): BitmapDrawable {
    val view = LayoutInflater.from(context).inflate(R.layout.view_map_marker, null)
    view.findViewById<TextView>(R.id.tvMarkerPrice).text = price

    view.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    val bitmap = createBitmap(view.measuredWidth, view.measuredHeight)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    return bitmap.toDrawable(context.resources)
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

                    // Oculta botones nativos para usar nuestros controles
                    zoomController.setVisibility(
                        org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER
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
                        icon = createMarkerIcon(context, apt.price)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    mapView.overlays.add(marker)
                }

                mapView.invalidate()
            }
        )

        ZoomControlsOverlay(
            onZoomIn = {
                mapRef?.let { map ->
                    val target = (map.zoomLevelDouble + zoomStep).coerceIn(minZoom, maxZoom)
                    map.controller.setZoom(target)
                }
            },
            onZoomOut = {
                mapRef?.let { map ->
                    val target = (map.zoomLevelDouble - zoomStep).coerceIn(minZoom, maxZoom)
                    map.controller.setZoom(target)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-24).dp)
        )

    }
}