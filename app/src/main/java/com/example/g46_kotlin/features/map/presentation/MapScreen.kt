package com.example.g46_kotlin.features.map.presentation

import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.example.g46_kotlin.features.map.presentation.components.MiniHouseCard
import com.example.g46_kotlin.features.map.presentation.components.MiniHouseCardUi
import android.graphics.Color as AndroidColor


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
    val scope = rememberCoroutineScope()
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
                selectedApartmentId = uiState.selectedApartmentId,
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

@Composable
private fun DrawerHandleHeader(title: String = "Recommended for you") {
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
private fun DrawerContent(
    apartments: List<PropertyPinUi>,
    selectedApartmentId: String?,
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
                    distanceToCampus = apt.description,
                    propertyType = "Apartment"
                ),
                onCardClick = { onApartmentClick(apt.id) }
            )
        }
    }
}



@Composable
fun MapScreen(
    onBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onApartmentClick: (id: String) -> Unit = {}
) {
    val viewModel: MapViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSettingsOverlay by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(hasLocationPermission(context))
    }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        hasLocationPermission = granted
        viewModel.startLocationTracking(granted)
    }

    val onApartmentTapped: (String) -> Unit = { apartmentId ->
        viewModel.onApartmentSelected(apartmentId)
        onApartmentClick(apartmentId)
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermission) {
            viewModel.startLocationTracking(true)
        } else if (!permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            viewModel.startLocationTracking(false)
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.stopLocationTracking() }
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

@Composable
private fun MapOverlayHost(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f))
            .clickable(onClick = onDismiss)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Evita que tap dentro del card cierre el overlay
        Box(modifier = Modifier.clickable(enabled = false, onClick = {})) {
            content()
        }
    }
}

@Composable
private fun MapSettingsOverlay(
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .fillMaxHeight(0.72f),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Text(
                text = "Ajustes del mapa",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Funcion en desarrollo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // TODO: Implementar mas tarde filtros para el mapa
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                )
            ) {
                Text(
                    text = "Pronto podras configurar filtros de precio, distancia y calificacion.",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

            FilledTonalButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text("Entendido")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MapSettingsOverlayPreview() {
    G46KotlinTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {
            MapSettingsOverlay()
        }
    }
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
                price = "$120"
            ),
            PropertyPinUi(
                id = "2",
                title = "City Lofts",
                description = "0.8 miles",
                rating = 4.6,
                lat = 4.6030,
                lon = -74.0670,
                price = "$450"
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

private fun hasLocationPermission(context: Context): Boolean {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fineGranted || coarseGranted
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

