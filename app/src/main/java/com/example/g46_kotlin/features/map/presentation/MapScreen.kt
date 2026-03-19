package com.example.g46_kotlin.features.map.presentation

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
    Map(
        userLocation = UserLocationUI(4.7110, -74.0721),
        apartments = listOf(
            ApartmentPinUi("1", "Preview apto", "Demo", 4.5, 4.7130, -74.0700)
        )
    )
}

@Composable
fun Map(
    userLocation: UserLocationUI?,
    apartments: List<ApartmentPinUi>
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Box(modifier = Modifier.fillMaxHeight()) {
            Image(
                painter = painterResource(id = R.drawable.map_example),
                contentDescription = "Mapa de ejemplo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        return
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(13.0)
            }
        },
        update = { mapView ->
            mapView.overlays.clear()

            userLocation?.let { user ->
                val userPoint = GeoPoint(user.lat, user.lon)
                mapView.controller.setCenter(userPoint)

                val userMarker = Marker(mapView).apply {
                    position = userPoint
                    title = "Tu ubicacion"
                }
                mapView.overlays.add(userMarker)
            }

            apartments.forEach { apt ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(apt.lat, apt.lon)
                    title = apt.title
                    snippet = "${apt.description} · ${apt.rating}"
                }
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
        }
    )
}