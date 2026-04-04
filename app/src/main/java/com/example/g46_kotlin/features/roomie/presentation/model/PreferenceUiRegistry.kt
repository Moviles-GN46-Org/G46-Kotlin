package com.example.g46_kotlin.features.roomie.presentation.model

import androidx.annotation.DrawableRes
import com.example.g46_kotlin.R
import com.example.g46_kotlin.features.roomie.domain.model.BooleanAspect
import com.example.g46_kotlin.features.roomie.domain.model.CleanlinessLevel
import com.example.g46_kotlin.features.roomie.domain.model.NoisePreference
import com.example.g46_kotlin.features.roomie.domain.model.PreferenceCategory
import com.example.g46_kotlin.features.roomie.domain.model.SleepSchedule

data class PreferenceUiSpec(
    val key: String,                 // Ej: "NIGHT_OWL"
    val category: PreferenceCategory,
    val label: String,               // Ej: "Night owl"
    @param:DrawableRes val iconRes: Int
)

data class BooleanAspectUiSpec(
    val aspect: BooleanAspect,
    val value: Boolean,
    val label: String,
    @param:DrawableRes val iconRes: Int
)

object PreferenceRegistry {

    // TODO: Reemplazar iconos cuando estén disponibles

    val all: List<PreferenceUiSpec> = listOf(
        // sleepSchedule
        PreferenceUiSpec(SleepSchedule.EARLY_BIRD.toString(), PreferenceCategory.SLEEP_SCHEDULE, "Early bird", R.drawable.ic_night_owl),
        PreferenceUiSpec(SleepSchedule.NIGHT_OWL.toString(), PreferenceCategory.SLEEP_SCHEDULE, "Night owl", R.drawable.ic_night_owl),
        PreferenceUiSpec(SleepSchedule.FLEXIBLE.toString(), PreferenceCategory.SLEEP_SCHEDULE, "Flexible", R.drawable.ic_night_owl),

        // cleanlinessLevel
        PreferenceUiSpec(CleanlinessLevel.VERY_TIDY.toString(), PreferenceCategory.CLEANLINESS_LEVEL, "Very tidy", R.drawable.ic_night_owl),
        PreferenceUiSpec(CleanlinessLevel.MODERATE.toString(), PreferenceCategory.CLEANLINESS_LEVEL, "Moderate", R.drawable.ic_night_owl),
        PreferenceUiSpec(CleanlinessLevel.RELAXED.toString(), PreferenceCategory.CLEANLINESS_LEVEL, "Relaxed", R.drawable.ic_night_owl),

        // noisePreference
        PreferenceUiSpec(NoisePreference.QUIET.toString(), PreferenceCategory.NOISE_PREFERENCE, "Quiet", R.drawable.ic_low_noise),
        PreferenceUiSpec(NoisePreference.MODERATE.toString(), PreferenceCategory.NOISE_PREFERENCE, "Moderate", R.drawable.ic_low_noise),
        PreferenceUiSpec(NoisePreference.LIVELY.toString(), PreferenceCategory.NOISE_PREFERENCE, "Lively", R.drawable.ic_low_noise)
    )

    private val byCategoryAndKey: Map<Pair<PreferenceCategory, String>, PreferenceUiSpec> =
        all.associateBy { it.category to it.key }

    fun find(category: PreferenceCategory, key: String): PreferenceUiSpec? =
        byCategoryAndKey[category to key]
}

object BooleanAspectRegistry {

    fun specFor(aspect: BooleanAspect, value: Boolean): BooleanAspectUiSpec {
        return when (aspect) {
            BooleanAspect.SMOKES -> {
                if (value) {
                    BooleanAspectUiSpec(
                        aspect = aspect,
                        value = true,
                        label = "Smoker",
                        iconRes = R.drawable.ic_smokes_yes
                    )
                } else {
                    BooleanAspectUiSpec(
                        aspect = aspect,
                        value = false,
                        label = "Non-smoker",
                        iconRes = R.drawable.ic_smokes_no
                    )
                }
            }

            BooleanAspect.HAS_PETS -> {
                if (value) {
                    BooleanAspectUiSpec(
                        aspect = aspect,
                        value = true,
                        label = "Pet friendly",
                        iconRes = R.drawable.ic_pet_lover
                    )
                } else {
                    BooleanAspectUiSpec(
                        aspect = aspect,
                        value = false,
                        label = "No pets",
                        iconRes = R.drawable.ic_pet_lover
                    )
                }
            }
        }
    }
}
