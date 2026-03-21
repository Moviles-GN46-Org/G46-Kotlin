package com.example.g46_kotlin.features.auth.presentation.signup

import com.example.g46_kotlin.features.auth.domain.model.UserRole

enum class HousingPlaceType {
    FULL_PLACE,
    OWN_ROOM,
    SHARED_ROOM
}

enum class SleepSchedule {
    EARLY_BIRD,
    NIGHT_OWL,
    FLEXIBLE
}

enum class CleanlinessLevel {
    VERY_TIDY,
    MODERATE,
    RELAXED
}

enum class NoisePreference {
    QUIET,
    MODERATE,
    LIVELY
}
data class SignupUiState(
    val currentStep: Int = 1,
    val selectedRole: UserRole = UserRole.STUDENT,
    val isLandlordEnabled: Boolean = false,

    val firstName: String = "",
    val lastName: String = "",
    val universityEmail: String = "",
    val password: String = "",

    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val universityEmailError: String? = null,
    val passwordError: String? = null,

    val isLoading: Boolean = false,
    val message: String? = null,

    val placeType: HousingPlaceType? = null,
    val monthlyBudget: String = "",
    val socialStratum: Int? = null,

    val wantsKitchen: Boolean = false,
    val wantsLaundry: Boolean = false,
    val wantsParking: Boolean = false,
    val wantsInternet: Boolean = false,

    val placeTypeError: String? = null,
    val monthlyBudgetError: String? = null,

    val sleepSchedule: SleepSchedule? = null,
    val cleanlinessLevel: CleanlinessLevel? = null,
    val noisePreference: NoisePreference? = null,
    val smokes: Boolean = false,
    val hasPets: Boolean = false,

    val bio: String = "",
    val budgetMin: String = "",
    val budgetMax: String = "",
    val preferredArea: String = "",
)