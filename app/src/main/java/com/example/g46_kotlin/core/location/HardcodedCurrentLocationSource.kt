package com.example.g46_kotlin.core.location

import javax.inject.Inject

class HardcodedCurrentLocationSource @Inject constructor() : CurrentLocationSource {
    override suspend fun getCurrentLocationOrNull(): AppLocation? {
// TODO: reemplazar por ubicación real
        return AppLocation(
            lat = 4.6016042953614225,
            lon = -74.06614174023011
        )
    }
}