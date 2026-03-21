package com.example.g46_kotlin.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

class FusedCurrentLocationSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) : CurrentLocationSource {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocationOrNull(): AppLocation? {
        if (!hasLocationPermission()) return null

        return try {
            getLastLocation()
        } catch (_: SecurityException) {
            null
        }
    }

    override fun observeLocationUpdates(): Flow<AppLocation> = callbackFlow {
        if (!hasLocationPermission()) {
            close()
            return@callbackFlow
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).setMinUpdateDistanceMeters(10f).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(AppLocation(it.latitude, it.longitude)) }
            }
        }

        try {
            client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        } catch (_: SecurityException) {
            close()
        }

        awaitClose { client.removeLocationUpdates(callback) }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): AppLocation? =
        suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { loc ->
                    cont.resume(loc?.let { AppLocation(it.latitude, it.longitude) })
                }
                .addOnFailureListener { cont.resume(null) }
        }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }
}
