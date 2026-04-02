package com.example.g46_kotlin.features.map.presentation.screen

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.animation.DecelerateInterpolator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.g46_kotlin.features.map.presentation.MapMarkerFactory
import com.example.g46_kotlin.features.map.presentation.PropertyPinUi
import com.example.g46_kotlin.features.map.presentation.UserLocationUI
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.collections.forEach
import android.graphics.Color as AndroidColor

@Composable
fun MapRender(
    userLocation: UserLocationUI?,
    apartments: List<PropertyPinUi>,
    onApartmentClick: (id: String) -> Unit = {}
) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val userMarkerColor = MaterialTheme.colorScheme.primary.toArgb()
    val userMarkerIcon = remember(userMarkerColor) {
        createUserLocationDrawable(context, userMarkerColor)
    }

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

                //Apartments markers
                apartments.forEach { apt ->
                    val marker = Marker(mapView).apply {
                        position = GeoPoint(apt.lat, apt.lon)
                        title = apt.title
                        icon = MapMarkerFactory.createMarkerIcon(context, apt.price)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                        setOnMarkerClickListener { _, _ ->
                            onApartmentClick(apt.id)
                            true
                        }
                    }
                    mapView.overlays.add(marker)
                }

                //User markers
                userLocation?.let { loc ->
                    val userMarker = Marker(mapView).apply {
                        position = GeoPoint(loc.lat, loc.lon)
                        title = "Your location"
                        icon = userMarkerIcon
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    }
                    mapView.overlays.add(userMarker)
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

@Composable
fun ZoomControlsOverlay(
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

private fun createUserLocationDrawable(
    context: Context,
    fillColor: Int
): Drawable {
    val density = context.resources.displayMetrics.density
    val sizePx = (16 * density).toInt()
    val strokePx = (2 * density).toInt()

    return GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setSize(sizePx, sizePx)
        setColor(fillColor)
        setStroke(strokePx, AndroidColor.WHITE)
    }
}