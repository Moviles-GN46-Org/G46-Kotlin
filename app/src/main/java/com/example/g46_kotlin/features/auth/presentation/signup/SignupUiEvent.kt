package com.example.g46_kotlin.features.auth.presentation.signup

import com.example.g46_kotlin.features.auth.domain.model.UserRole


sealed interface SignupUiEvent {
    data class OnRoleSelected(val role: UserRole) : SignupUiEvent
    data class OnFirstNameChanged(val value: String) : SignupUiEvent
    data class OnLastNameChanged(val value: String) : SignupUiEvent
    data class OnUniversityEmailChanged(val value: String) : SignupUiEvent
    data class OnPasswordChanged(val value: String) : SignupUiEvent

    data object OnNextStep : SignupUiEvent
    data object OnPreviousStep : SignupUiEvent
    data object OnSubmit : SignupUiEvent

    data class OnPlaceTypeSelected(val value: HousingPlaceType) : SignupUiEvent
    data class OnMonthlyBudgetChanged(val value: String) : SignupUiEvent
    data class OnSocialStratumSelected(val value: Int) : SignupUiEvent

    data class OnKitchenToggle(val value: Boolean) : SignupUiEvent
    data class OnLaundryToggle(val value: Boolean) : SignupUiEvent
    data class OnParkingToggle(val value: Boolean) : SignupUiEvent
    data class OnInternetToggle(val value: Boolean) : SignupUiEvent
}