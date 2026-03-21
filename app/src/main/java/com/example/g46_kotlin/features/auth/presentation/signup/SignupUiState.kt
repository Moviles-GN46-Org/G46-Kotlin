package com.example.g46_kotlin.features.auth.presentation.signup

import com.example.g46_kotlin.features.auth.domain.model.UserRole

enum class HousingPlaceType {
    FULL_PLACE,
    OWN_ROOM,
    SHARED_ROOM
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
)