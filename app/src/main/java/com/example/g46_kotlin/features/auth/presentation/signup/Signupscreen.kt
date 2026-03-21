package com.example.g46_kotlin.features.auth.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.ui.Alignment


@Composable
fun SignupScreen(
    onBackClick: () -> Unit,
    onSignupFinished: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SignupEffect.ShowMessage -> onShowMessage(effect.message)
                SignupEffect.Finished -> onSignupFinished()
            }
        }
    }

    SignupContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignupContent(
    uiState: SignupUiState,
    onBackClick: () -> Unit,
    onEvent: (SignupUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Step ${uiState.currentStep} of 5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState.currentStep) {
                    1 -> StepRole(uiState = uiState, onEvent = onEvent)
                    2 -> StepAccountBasics(uiState = uiState, onEvent = onEvent)
                    3 -> StepHousingNeeds(uiState = uiState, onEvent = onEvent)
                    4 -> StepLifestyle(uiState = uiState, onEvent = onEvent)
                    5 -> StepProfile(uiState, onEvent)
                }


                Spacer(modifier = Modifier.height(20.dp))
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (uiState.currentStep == 1) onBackClick()
                            else onEvent(SignupUiEvent.OnPreviousStep)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    ) {
                        Text(if (uiState.currentStep == 1) "Back to Login" else "Back")
                    }

                    Button(
                        onClick = {
                            if (uiState.currentStep == 5) onEvent(SignupUiEvent.OnSubmit)
                            else onEvent(SignupUiEvent.OnNextStep)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading
                    ) {
                        Text(if (uiState.currentStep == 5) "Submit" else "Continue")
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRole(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Choose account type",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { onEvent(SignupUiEvent.OnRoleSelected(UserRole.STUDENT)) },
            modifier = Modifier.weight(1f),
            enabled = !uiState.isLoading
        ) {
            Text("I'm a student")
        }

        OutlinedButton(
            onClick = { onEvent(SignupUiEvent.OnRoleSelected(UserRole.LANDLORD)) },
            modifier = Modifier.weight(1f),
            enabled = uiState.isLandlordEnabled && !uiState.isLoading
        ) {
            Text("I'm a landlord")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Landlord option is visible but disabled for now.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StepAccountBasics(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    OutlinedTextField(
        value = uiState.firstName,
        onValueChange = { onEvent(SignupUiEvent.OnFirstNameChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("First name") },
        enabled = !uiState.isLoading,
        singleLine = true,
        isError = uiState.firstNameError != null
    )
    if (uiState.firstNameError != null) {
        Text(
            text = uiState.firstNameError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = uiState.lastName,
        onValueChange = { onEvent(SignupUiEvent.OnLastNameChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Last name") },
        enabled = !uiState.isLoading,
        singleLine = true,
        isError = uiState.lastNameError != null
    )
    if (uiState.lastNameError != null) {
        Text(
            text = uiState.lastNameError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = uiState.universityEmail,
        onValueChange = { onEvent(SignupUiEvent.OnUniversityEmailChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("University email") },
        enabled = !uiState.isLoading,
        singleLine = true,
        isError = uiState.universityEmailError != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    if (uiState.universityEmailError != null) {
        Text(
            text = uiState.universityEmailError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = uiState.password,
        onValueChange = { onEvent(SignupUiEvent.OnPasswordChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Password") },
        enabled = !uiState.isLoading,
        singleLine = true,
        isError = uiState.passwordError != null,
        visualTransformation = PasswordVisualTransformation()
    )
    if (uiState.passwordError != null) {
        Text(
            text = uiState.passwordError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun StepPlaceholder(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Pending UI details for this step.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StepHousingNeeds(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Housing needs",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectableHousingButton(
            label = "Full place",
            isSelected = uiState.placeType == HousingPlaceType.FULL_PLACE,
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.FULL_PLACE)) },
            modifier = Modifier.weight(1f)
        )

        SelectableHousingButton(
            label = "Own room",
            isSelected = uiState.placeType == HousingPlaceType.OWN_ROOM,
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.OWN_ROOM)) },
            modifier = Modifier.weight(1f)
        )

        SelectableHousingButton(
            label = "Shared room",
            isSelected = uiState.placeType == HousingPlaceType.SHARED_ROOM,
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.SHARED_ROOM)) },
            modifier = Modifier.weight(1f)
        )
    }

    if (uiState.placeTypeError != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = uiState.placeTypeError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = uiState.monthlyBudget,
        onValueChange = { onEvent(SignupUiEvent.OnMonthlyBudgetChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Monthly budget") },
        singleLine = true,
        isError = uiState.monthlyBudgetError != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    if (uiState.monthlyBudgetError != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = uiState.monthlyBudgetError,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SelectableHousingButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(label)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(label)
        }
    }
}

@Composable
private fun StepLifestyle(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Lifestyle preferences",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Sleep schedule",
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectableLifestyleButton(
            label = "Early bird",
            isSelected = uiState.sleepSchedule == SleepSchedule.EARLY_BIRD,
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.EARLY_BIRD)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Night owl",
            isSelected = uiState.sleepSchedule == SleepSchedule.NIGHT_OWL,
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.NIGHT_OWL)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Flexible",
            isSelected = uiState.sleepSchedule == SleepSchedule.FLEXIBLE,
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.FLEXIBLE)) },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Cleanliness level",
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectableLifestyleButton(
            label = "Very tidy",
            isSelected = uiState.cleanlinessLevel == CleanlinessLevel.VERY_TIDY,
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.VERY_TIDY)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Moderate",
            isSelected = uiState.cleanlinessLevel == CleanlinessLevel.MODERATE,
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.MODERATE)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Relaxed",
            isSelected = uiState.cleanlinessLevel == CleanlinessLevel.RELAXED,
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.RELAXED)) },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Noise preference",
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectableLifestyleButton(
            label = "Quiet",
            isSelected = uiState.noisePreference == NoisePreference.QUIET,
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.QUIET)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Moderate",
            isSelected = uiState.noisePreference == NoisePreference.MODERATE,
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.MODERATE)) },
            modifier = Modifier.weight(1f)
        )
        SelectableLifestyleButton(
            label = "Lively",
            isSelected = uiState.noisePreference == NoisePreference.LIVELY,
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.LIVELY)) },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Checkbox(
                checked = uiState.smokes,
                onCheckedChange = { onEvent(SignupUiEvent.OnSmokesToggle(it)) }
            )
            Text("Smokes", style = MaterialTheme.typography.bodyMedium)
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Checkbox(
                checked = uiState.hasPets,
                onCheckedChange = { onEvent(SignupUiEvent.OnHasPetsToggle(it)) }
            )
            Text("Has pets", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SelectableLifestyleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSelected) {
        Button(onClick = onClick, modifier = modifier) {
            Text(label)
        }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) {
            Text(label)
        }
    }
}

@Composable
private fun StepProfile(
    state: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tell us about yourself",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Complete your roommate profile",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Bio field
        OutlinedTextField(
            value = state.bio,
            onValueChange = { onEvent(SignupUiEvent.OnBioChanged(it)) },
            label = { Text("Bio") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            minLines = 4,
            maxLines = 6
        )

        // Budget Min field
        OutlinedTextField(
            value = state.budgetMin,
            onValueChange = { onEvent(SignupUiEvent.OnBudgetMinChanged(it)) },
            label = { Text("Minimum Monthly Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Budget Max field
        OutlinedTextField(
            value = state.budgetMax,
            onValueChange = { onEvent(SignupUiEvent.OnBudgetMaxChanged(it)) },
            label = { Text("Maximum Monthly Budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Preferred Area field
        OutlinedTextField(
            value = state.preferredArea,
            onValueChange = { onEvent(SignupUiEvent.OnPreferredAreaChanged(it)) },
            label = { Text("Preferred Area/City") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "Signup Step 1")
@Composable
private fun SignupStep1Preview() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(currentStep = 1),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup Step 2")
@Composable
private fun SignupStep2Preview() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 2,
                firstName = "Jane",
                lastName = "Doe",
                universityEmail = "jane@uni.edu"
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup Step 3")
@Composable
private fun SignupStep3Preview() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 3,
                placeType = HousingPlaceType.OWN_ROOM,
                monthlyBudget = "800"
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup Step 4")
@Composable
private fun SignupStep4Preview() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 4,
                sleepSchedule = SleepSchedule.NIGHT_OWL,
                cleanlinessLevel = CleanlinessLevel.MODERATE,
                noisePreference = NoisePreference.MODERATE,
                smokes = false,
                hasPets = true
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup Step 5")
@Composable
private fun SignupScreenStep5Preview() {
    val state = SignupUiState(
        currentStep = 5,
        bio = "Love cooking and going out! Looking for a quiet apartment with good vibes.",
        budgetMin = "500",
        budgetMax = "800",
        preferredArea = "Downtown"
    )

    G46KotlinTheme {
        SignupContent(
            uiState = state,
            onBackClick = {},
            onEvent = {}
        )
    }
}