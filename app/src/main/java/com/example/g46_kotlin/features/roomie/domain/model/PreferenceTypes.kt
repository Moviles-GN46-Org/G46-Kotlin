package com.example.g46_kotlin.features.roomie.domain.model

enum class PreferenceCategory {
    SLEEP_SCHEDULE,
    CLEANLINESS_LEVEL,
    NOISE_PREFERENCE
}

enum class BooleanAspect {
    SMOKES,
    HAS_PETS
}

enum class SleepSchedule(val value: String) {
    EARLY_BIRD("EARLY_BIRD"),
    NIGHT_OWL("NIGHT_OWL"),
    FLEXIBLE("FLEXIBLE");

    override fun toString(): String = value

    companion object {
        fun fromValue(value: String): SleepSchedule? =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
    }
}

enum class CleanlinessLevel(val value: String) {
    VERY_TIDY("VERY_TIDY"),
    MODERATE("MODERATE"),
    RELAXED("RELAXED");

    override fun toString(): String = value

    companion object {
        fun fromValue(value: String): CleanlinessLevel? =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
    }
}

enum class NoisePreference(val value: String) {
    QUIET("QUIET"),
    MODERATE("MODERATE"),
    LIVELY("LIVELY");

    override fun toString(): String = value

    companion object {
        fun fromValue(value: String): NoisePreference? =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
    }
}

