package com.example.g46_kotlin.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface SignupEffect {
    data class ShowMessage(val message: String) : SignupEffect
    data object Finished : SignupEffect
}

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SignupEffect>()
    val effects: SharedFlow<SignupEffect> = _effects.asSharedFlow()

    fun onEvent(event: SignupUiEvent) {
        when (event) {
            is SignupUiEvent.OnRoleSelected -> onRoleSelected(event.role)

            is SignupUiEvent.OnFirstNameChanged -> {
                _uiState.update {
                    it.copy(
                        firstName = event.value,
                        firstNameError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnLastNameChanged -> {
                _uiState.update {
                    it.copy(
                        lastName = event.value,
                        lastNameError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnUniversityEmailChanged -> {
                _uiState.update {
                    it.copy(
                        universityEmail = event.value,
                        universityEmailError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnPlaceTypeSelected -> {
                _uiState.update {
                    it.copy(
                        placeType = event.value,
                        placeTypeError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnMonthlyBudgetChanged -> {
                _uiState.update {
                    it.copy(
                        monthlyBudget = event.value,
                        monthlyBudgetError = null,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnSocialStratumSelected -> {
                _uiState.update {
                    it.copy(
                        socialStratum = event.value,
                        message = null
                    )
                }
            }

            is SignupUiEvent.OnKitchenToggle -> {
                _uiState.update { it.copy(wantsKitchen = event.value, message = null) }
            }

            is SignupUiEvent.OnLaundryToggle -> {
                _uiState.update { it.copy(wantsLaundry = event.value, message = null) }
            }

            is SignupUiEvent.OnParkingToggle -> {
                _uiState.update { it.copy(wantsParking = event.value, message = null) }
            }

            is SignupUiEvent.OnInternetToggle -> {
                _uiState.update { it.copy(wantsInternet = event.value, message = null) }
            }

            SignupUiEvent.OnNextStep -> onNextStep()
            SignupUiEvent.OnPreviousStep -> onPreviousStep()
            SignupUiEvent.OnSubmit -> onSubmit()
        }
    }

    private fun onRoleSelected(role: UserRole) {
        if (role == UserRole.LANDLORD && !_uiState.value.isLandlordEnabled) {
            emitMessage("Landlord signup pending")
            return
        }

        _uiState.update {
            it.copy(
                selectedRole = role,
                message = null
            )
        }
    }

    private fun onNextStep() {
        val state = _uiState.value

        when (state.currentStep) {
            1 -> {
                _uiState.update { it.copy(currentStep = 2, message = null) }
            }

            2 -> {
                if (!validateStep2()) return
                _uiState.update { it.copy(currentStep = 3, message = null) }
            }

            3 -> {
                if (!validateStep3()) return
                _uiState.update { it.copy(currentStep = 4, message = null) }
            }
            4 -> _uiState.update { it.copy(currentStep = 5, message = null) }
            5 -> onSubmit()
        }
    }

    private fun onPreviousStep() {
        _uiState.update { state ->
            val prev = (state.currentStep - 1).coerceAtLeast(1)
            state.copy(currentStep = prev, message = null)
        }
    }

    private fun onSubmit() {
        val state = _uiState.value

        if (state.currentStep < 5) {
            emitMessage("Complete all steps before submitting")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
            _effects.emit(SignupEffect.ShowMessage("Signup pending backend integration"))
            _effects.emit(SignupEffect.Finished)
        }
    }

    private fun validateStep2(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.trim().isBlank()) "First name is required" else null
        val lastNameError = if (state.lastName.trim().isBlank()) "Last name is required" else null

        val email = state.universityEmail.trim()
        val universityEmailError = when {
            email.isBlank() -> "University email is required"
            !isValidEmail(email) -> "Enter a valid university email"
            else -> null
        }

        val passwordError = when {
            state.password.isBlank() -> "Password is required"
            state.password.length < 6 -> "Password must have at least 6 characters"
            else -> null
        }

        _uiState.update {
            it.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                universityEmailError = universityEmailError,
                passwordError = passwordError
            )
        }

        return firstNameError == null &&
                lastNameError == null &&
                universityEmailError == null &&
                passwordError == null
    }

    private fun validateStep3(): Boolean {
        val state = _uiState.value

        val placeTypeError = if (state.placeType == null) "Select a place type" else null

        val budgetValue = state.monthlyBudget.trim()
        val monthlyBudgetError = when {
            budgetValue.isBlank() -> "Monthly budget is required"
            budgetValue.toIntOrNull() == null -> "Enter a valid number"
            budgetValue.toInt() <= 0 -> "Budget must be greater than 0"
            else -> null
        }

        _uiState.update {
            it.copy(
                placeTypeError = placeTypeError,
                monthlyBudgetError = monthlyBudgetError
            )
        }

        return placeTypeError == null && monthlyBudgetError == null
    }

    private fun emitMessage(message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(message = message) }
            _effects.emit(SignupEffect.ShowMessage(message))
        }
    }

    private fun isValidEmail(value: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(value)
    }
}