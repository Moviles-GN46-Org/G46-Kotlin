package com.example.g46_kotlin.features.roomie.presentation.mapper

import com.example.g46_kotlin.features.roomie.domain.model.PreferenceCategory
import com.example.g46_kotlin.features.roomie.presentation.model.PreferenceRegistry
import com.example.g46_kotlin.features.roomie.presentation.model.PreferenceUiSpec

fun mapMultiOptionPreferences(
    sleepSchedule: String?,
    cleanlinessLevel: String?,
    noisePreference: String?
): List<PreferenceUiSpec> {
    return buildList {
        sleepSchedule?.let {
            PreferenceRegistry.find(PreferenceCategory.SLEEP_SCHEDULE, it)?.let(::add)
        }
        cleanlinessLevel?.let {
            PreferenceRegistry.find(PreferenceCategory.CLEANLINESS_LEVEL, it)?.let(::add)
        }
        noisePreference?.let {
            PreferenceRegistry.find(PreferenceCategory.NOISE_PREFERENCE, it)?.let(::add)
        }
    }
}