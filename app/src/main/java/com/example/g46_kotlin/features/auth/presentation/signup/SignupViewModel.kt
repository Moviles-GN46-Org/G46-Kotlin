package com.example.g46_kotlin.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.auth.domain.model.AuthResult
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.features.auth.domain.repository.AuthRepository
import com.example.g46_kotlin.features.auth.domain.repository.RegisterParams
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

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

            is SignupUiEvent.OnSleepScheduleSelected -> {
                _uiState.update { it.copy(sleepSchedule = event.value, message = null) }
            }

            is SignupUiEvent.OnCleanlinessSelected -> {
                _uiState.update { it.copy(cleanlinessLevel = event.value, message = null) }
            }

            is SignupUiEvent.OnNoisePreferenceSelected -> {
                _uiState.update { it.copy(noisePreference = event.value, message = null) }
            }

            is SignupUiEvent.OnSmokesToggle -> {
                _uiState.update { it.copy(smokes = event.value, message = null) }
            }

            is SignupUiEvent.OnHasPetsToggle -> {
                _uiState.update { it.copy(hasPets = event.value, message = null) }
            }

            is SignupUiEvent.OnBioChanged -> {
                _uiState.update { it.copy(bio = event.bio, message = null) }
            }

            is SignupUiEvent.OnBudgetMinChanged -> {
                _uiState.update { it.copy(budgetMin = event.budgetMin, message = null) }
            }

            is SignupUiEvent.OnBudgetMaxChanged -> {
                _uiState.update { it.copy(budgetMax = event.budgetMax, message = null) }
            }

            is SignupUiEvent.OnPreferredAreaChanged -> {
                _uiState.update { it.copy(preferredArea = event.preferredArea, message = null) }
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

            4 -> {
                if (!validateStep4()) return
                _uiState.update { it.copy(currentStep = 5, message = null) }
            }

            5 -> {
                onSubmit()
            }
        }
    }

    private fun onPreviousStep() {
        _uiState.update { state ->
            val prev = (state.currentStep - 1).coerceAtLeast(1)
            state.copy(currentStep = prev, message = null)
        }
    }

    // TODO: hacer una verificacion de email real (register -> verify-email -> refresh token)
    private fun onSubmit() {
        val state = _uiState.value

        if (state.currentStep < 5) {
            emitMessage("Complete all steps before submitting")
            return
        }

        if (!validateStep5()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, message = null) }

            val result = authRepository.register(
                RegisterParams(
                    email = state.universityEmail.trim(),
                    password = state.password,
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    role = state.selectedRole
                )
            )

            when (result) {
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, message = result.message) }
                    _effects.emit(SignupEffect.ShowMessage(result.message))
                }

                is AuthResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            message = null,
                            password = ""
                        )
                    }
                    _effects.emit(SignupEffect.ShowMessage("Account created successfully. Please log in."))
                    _effects.emit(SignupEffect.Finished)
                }
            }
        }
    }

    private fun validateStep2(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.trim().isBlank()) "First name is required" else null
        val lastNameError = if (state.lastName.trim().isBlank()) "Last name is required" else null

        val email = state.universityEmail.trim()
        val isStudent = state.selectedRole == UserRole.STUDENT

        val universityEmailError = when {
            email.isBlank() -> "University email is required"
            !isValidEmail(email) -> "Enter a valid university email"
            isStudent && !isAcademicStudentEmail(email) -> "Students must use an academic .edu email"
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

    private fun validateStep4(): Boolean {
        val state = _uiState.value

        val sleepScheduleError = if (state.sleepSchedule == null) "Select a sleep schedule" else null
        val cleanlinessError = if (state.cleanlinessLevel == null) "Select cleanliness level" else null
        val noiseError = if (state.noisePreference == null) "Select noise preference" else null

        _uiState.update {
            it.copy(
                message = if (sleepScheduleError != null || cleanlinessError != null || noiseError != null) {
                    "Please complete all fields"
                } else {
                    null
                }
            )
        }

        return sleepScheduleError == null && cleanlinessError == null && noiseError == null
    }

    private fun validateStep5(): Boolean {
        val state = uiState.value
        if (state.bio.isBlank()) {
            _uiState.update { it.copy(message = "Bio is required") }
            return false
        }
        if (state.budgetMin.isBlank() || state.budgetMin.toIntOrNull() == null || state.budgetMin.toInt() <= 0) {
            _uiState.update { it.copy(message = "Minimum budget must be a positive number") }
            return false
        }
        if (state.budgetMax.isBlank() || state.budgetMax.toIntOrNull() == null || state.budgetMax.toInt() <= 0) {
            _uiState.update { it.copy(message = "Maximum budget must be a positive number") }
            return false
        }
        if (state.budgetMin.toInt() > state.budgetMax.toInt()) {
            _uiState.update { it.copy(message = "Minimum budget cannot exceed maximum budget") }
            return false
        }
        if (state.preferredArea.isBlank()) {
            _uiState.update { it.copy(message = "Preferred area is required") }
            return false
        }
        return true
    }

    private fun emitMessage(message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(message = message) }
            _effects.emit(SignupEffect.ShowMessage(message))
        }
    }

    private fun isValidEmail(value: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(value.trim())
    }

    private fun isAcademicStudentEmail(value: String): Boolean {
        val academicEmailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.edu(\\.[A-Za-z]{2,})?$".toRegex()
        return academicEmailRegex.matches(value.trim().lowercase())
    }
}