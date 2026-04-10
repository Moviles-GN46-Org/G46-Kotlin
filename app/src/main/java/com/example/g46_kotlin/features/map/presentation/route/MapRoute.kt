package com.example.g46_kotlin.features.map.presentation.route

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.map.presentation.screen.MapScreen
import com.example.g46_kotlin.features.map.presentation.MapViewModel

@Composable
fun MapRoute(
    onBackClick: () -> Unit,
    onPropertyClick: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(hasLocationPermission(context))
    }

    var permissionRequested by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted =
            grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        hasLocationPermission = granted
        viewModel.startLocationTracking(granted)
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
        onDispose {
            viewModel.stopLocationTracking()
        }
    }

    //TODO: Implementar configuración/filtros del mapa
    MapScreen(
        uiState = uiState,
        onBack = onBackClick,
        onSettingsClick = {},
        onPropertyClick = onPropertyClick,
        onApartmentSelected = viewModel::onApartmentSelected,
        onCameraChanged = viewModel::onCameraChanged
    )
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