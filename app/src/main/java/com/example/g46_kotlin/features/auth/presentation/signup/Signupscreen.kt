package com.example.g46_kotlin.features.auth.presentation.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.g46_kotlin.features.auth.domain.model.UserRole
import com.example.g46_kotlin.ui.theme.G46KotlinTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.example.g46_kotlin.R
import com.example.g46_kotlin.ui.theme.DeepMocha
import com.example.g46_kotlin.ui.theme.DustyTaupe
import com.example.g46_kotlin.ui.theme.LightBronze
import com.example.g46_kotlin.ui.theme.WarmLinen

private val panelShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
private val inputShape = RoundedCornerShape(17.dp)
private val buttonShape = RoundedCornerShape(14.dp)
private val chipShape = RoundedCornerShape(999.dp)

@Composable
fun SignupScreen(
    onBackClick: () -> Unit,
    onSignupFinished: () -> Unit,
    onShowMessage: (String) -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignupContent(
    uiState: SignupUiState,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onBackClick: () -> Unit,
    onEvent: (SignupUiEvent) -> Unit
) {
    val topOffset = 235.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_image),
            contentDescription = "Signup header image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(topOffset + 10.dp)
                .graphicsLayer {
                    scaleX = 1.2f
                    scaleY = 1.2f
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
                .align(Alignment.TopCenter)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = topOffset),
            shape = panelShape,
            colors = CardDefaults.cardColors(containerColor = WarmLinen),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 28.dp)
            ) {
                SignupHeader(currentStep = uiState.currentStep)

                Spacer(modifier = Modifier.height(14.dp))
                SignupProgressDots(currentStep = uiState.currentStep, totalSteps = 5)

                Spacer(modifier = Modifier.height(18.dp))

                when (uiState.currentStep) {
                    1 -> StepRole(uiState = uiState, onEvent = onEvent)
                    2 -> StepAccountBasics(
                        uiState = uiState,
                        passwordVisible = passwordVisible,
                        onTogglePasswordVisibility = onTogglePasswordVisibility,
                        onEvent = onEvent
                    )
                    3 -> StepHousingNeeds(uiState = uiState, onEvent = onEvent)
                    4 -> StepLifestyle(uiState = uiState, onEvent = onEvent)
                    5 -> StepProfile(uiState = uiState, onEvent = onEvent)
                }

                uiState.message?.let { message ->
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = LightBronze.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            if (uiState.currentStep == 1) onBackClick()
                            else onEvent(SignupUiEvent.OnPreviousStep)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        enabled = !uiState.isLoading,
                        shape = buttonShape,
                        border = BorderStroke(1.dp, LightBronze.copy(alpha = 0.45f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LightBronze.copy(alpha = 0.10f),
                            contentColor = LightBronze,
                            disabledContainerColor = LightBronze.copy(alpha = 0.08f),
                            disabledContentColor = LightBronze.copy(alpha = 0.45f)
                        )
                    ) {
                        Text(
                            text = if (uiState.currentStep == 1) "Go back" else "Back",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = {
                            if (uiState.currentStep == 5) onEvent(SignupUiEvent.OnSubmit)
                            else onEvent(SignupUiEvent.OnNextStep)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        enabled = !uiState.isLoading,
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightBronze,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = if (uiState.currentStep == 5) "Get started!" else "Continue",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SignupHeader(currentStep: Int) {
    val subtitle = when (currentStep) {
        1 -> "Choose how you want to use Casandes"
        2 -> "Step 1 of 4: Account Basics"
        3 -> "Step 2 of 4: Housing Needs"
        4 -> "Step 3 of 4: Lifestyle"
        5 -> "Step 4 of 4: Your Profile"
        else -> "Sign up"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sign up",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = DeepMocha,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = DustyTaupe
        )
    }
}

@Composable
private fun SignupProgressDots(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 1..totalSteps) {
            val active = index <= currentStep
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (active) LightBronze else LightBronze.copy(alpha = 0.22f),
                        shape = CircleShape
                    )
            )

            if (index < totalSteps) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(
                            color = if (index < currentStep) {
                                LightBronze.copy(alpha = 0.6f)
                            } else {
                                LightBronze.copy(alpha = 0.18f)
                            }
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
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
        text = "Let's get your account set up in just a few steps",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "What brings you here today?",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )
    Spacer(modifier = Modifier.height(10.dp))

    RoleOptionCard(
        title = "I'm a student",
        description = "Browse rooms, apartments, and find roommates",
        selected = uiState.selectedRole == UserRole.STUDENT,
        enabled = !uiState.isLoading,
        onClick = { onEvent(SignupUiEvent.OnRoleSelected(UserRole.STUDENT)) }
    )

    Spacer(modifier = Modifier.height(10.dp))

    RoleOptionCard(
        title = "I'm a landlord",
        description = "List your property and find reliable tenants",
        selected = uiState.selectedRole == UserRole.LANDLORD,
        enabled = uiState.isLandlordEnabled && !uiState.isLoading,
        onClick = { onEvent(SignupUiEvent.OnRoleSelected(UserRole.LANDLORD)) }
    )

    if (!uiState.isLandlordEnabled) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Landlord option is visible but disabled for now.",
            style = MaterialTheme.typography.bodySmall,
            color = DustyTaupe
        )
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) LightBronze else LightBronze.copy(alpha = 0.30f)
    val cardColor = if (selected) LightBronze.copy(alpha = 0.16f) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = DeepMocha
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = DustyTaupe
            )
        }
    }
}

@Composable
private fun StepAccountBasics(
    uiState: SignupUiState,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Let's get your account set up as a student",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SignupTextField(
            value = uiState.firstName,
            onValueChange = { onEvent(SignupUiEvent.OnFirstNameChanged(it)) },
            label = "First name",
            placeholder = "Jane",
            error = uiState.firstNameError,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f)
        )

        SignupTextField(
            value = uiState.lastName,
            onValueChange = { onEvent(SignupUiEvent.OnLastNameChanged(it)) },
            label = "Last name",
            placeholder = "Doe",
            error = uiState.lastNameError,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    SignupTextField(
        value = uiState.universityEmail,
        onValueChange = { onEvent(SignupUiEvent.OnUniversityEmailChanged(it)) },
        label = "University email",
        placeholder = "janedoe@university.edu",
        error = uiState.universityEmailError,
        enabled = !uiState.isLoading,
        keyboardType = KeyboardType.Email
    )

    Spacer(modifier = Modifier.height(10.dp))

    SignupPasswordField(
        value = uiState.password,
        onValueChange = { onEvent(SignupUiEvent.OnPasswordChanged(it)) },
        label = "Password",
        placeholder = "Create a secure password",
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        error = uiState.passwordError,
        enabled = !uiState.isLoading
    )
}

@Composable
private fun StepHousingNeeds(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "To show you the most relevant listings",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(12.dp))

    SectionLabel(text = "Type of place")

    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SignupChoiceChip(
            label = "Full place",
            selected = uiState.placeType == HousingPlaceType.FULL_PLACE,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.FULL_PLACE)) }
        )
        SignupChoiceChip(
            label = "Own room",
            selected = uiState.placeType == HousingPlaceType.OWN_ROOM,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.OWN_ROOM)) }
        )
        SignupChoiceChip(
            label = "Shared room",
            selected = uiState.placeType == HousingPlaceType.SHARED_ROOM,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnPlaceTypeSelected(HousingPlaceType.SHARED_ROOM)) }
        )
    }

    uiState.placeTypeError?.let {
        Spacer(modifier = Modifier.height(6.dp))
        ErrorText(text = it)
    }

    Spacer(modifier = Modifier.height(12.dp))

    SectionLabel(text = "Monthly budget")

    val sliderBudget = uiState.monthlyBudget.toFloatOrNull()?.coerceIn(100f, 5000f) ?: 500f
    Text(
        text = "$${sliderBudget.toInt()}",
        style = MaterialTheme.typography.titleLarge.copy(
            color = DeepMocha,
            fontWeight = FontWeight.Bold
        )
    )

    Slider(
        value = sliderBudget,
        onValueChange = { onEvent(SignupUiEvent.OnMonthlyBudgetChanged(it.toInt().toString())) },
        valueRange = 100f..5000f,
        enabled = !uiState.isLoading
    )

    SignupTextField(
        value = uiState.monthlyBudget,
        onValueChange = { onEvent(SignupUiEvent.OnMonthlyBudgetChanged(it)) },
        label = "Custom budget",
        placeholder = "1200",
        error = uiState.monthlyBudgetError,
        enabled = !uiState.isLoading,
        keyboardType = KeyboardType.Number
    )

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Social stratum")

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "STRATUM 1",
            selected = uiState.socialStratum == 1,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(1)) }
        )
        SignupTagChip(
            text = "STRATUM 2",
            selected = uiState.socialStratum == 2,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(2)) }
        )
        SignupTagChip(
            text = "STRATUM 3",
            selected = uiState.socialStratum == 3,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(3)) }
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "STRATUM 4",
            selected = uiState.socialStratum == 4,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(4)) }
        )
        SignupTagChip(
            text = "STRATUM 5",
            selected = uiState.socialStratum == 5,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(5)) }
        )
        SignupTagChip(
            text = "STRATUM 6",
            selected = uiState.socialStratum == 6,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSocialStratumSelected(6)) }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Utilities and attributes")

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "KITCHEN",
            selected = uiState.wantsKitchen,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnKitchenToggle(!uiState.wantsKitchen)) }
        )
        SignupTagChip(
            text = "LAUNDRY",
            selected = uiState.wantsLaundry,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnLaundryToggle(!uiState.wantsLaundry)) }
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "PARKING",
            selected = uiState.wantsParking,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnParkingToggle(!uiState.wantsParking)) }
        )
        SignupTagChip(
            text = "INTERNET",
            selected = uiState.wantsInternet,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnInternetToggle(!uiState.wantsInternet)) }
        )
    }
}

@Composable
private fun StepLifestyle(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Help potential roommate and landlords know what to expect",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Sleep schedule")
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "EARLY BIRD",
            selected = uiState.sleepSchedule == SleepSchedule.EARLY_BIRD,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.EARLY_BIRD)) }
        )
        SignupTagChip(
            text = "NIGHT OWL",
            selected = uiState.sleepSchedule == SleepSchedule.NIGHT_OWL,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.NIGHT_OWL)) }
        )
        SignupTagChip(
            text = "FLEXIBLE",
            selected = uiState.sleepSchedule == SleepSchedule.FLEXIBLE,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSleepScheduleSelected(SleepSchedule.FLEXIBLE)) }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Noise level")
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "QUIET",
            selected = uiState.noisePreference == NoisePreference.QUIET,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.QUIET)) }
        )
        SignupTagChip(
            text = "MODERATE",
            selected = uiState.noisePreference == NoisePreference.MODERATE,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.MODERATE)) }
        )
        SignupTagChip(
            text = "LIVELY",
            selected = uiState.noisePreference == NoisePreference.LIVELY,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnNoisePreferenceSelected(NoisePreference.LIVELY)) }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Cleanliness")
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "RELAXED",
            selected = uiState.cleanlinessLevel == CleanlinessLevel.RELAXED,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.RELAXED)) }
        )
        SignupTagChip(
            text = "TIDY",
            selected = uiState.cleanlinessLevel == CleanlinessLevel.MODERATE,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.MODERATE)) }
        )
        SignupTagChip(
            text = "NEAT FREAK",
            selected = uiState.cleanlinessLevel == CleanlinessLevel.VERY_TIDY,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnCleanlinessSelected(CleanlinessLevel.VERY_TIDY)) }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    SectionLabel(text = "Personal habits")
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SignupTagChip(
            text = "SMOKES",
            selected = uiState.smokes,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnSmokesToggle(!uiState.smokes)) }
        )
        SignupTagChip(
            text = "HAS PETS",
            selected = uiState.hasPets,
            enabled = !uiState.isLoading,
            modifier = Modifier.weight(1f),
            onClick = { onEvent(SignupUiEvent.OnHasPetsToggle(!uiState.hasPets)) }
        )
    }
}

@Composable
private fun StepProfile(
    uiState: SignupUiState,
    onEvent: (SignupUiEvent) -> Unit
) {
    Text(
        text = "Let other know a bit more about you!",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "Final details for your profile",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = DeepMocha
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = LightBronze.copy(alpha = 0.35f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = LightBronze,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Profile picture (optional)",
                style = MaterialTheme.typography.bodySmall,
                color = DustyTaupe,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    SignupTextField(
        value = uiState.bio,
        onValueChange = { onEvent(SignupUiEvent.OnBioChanged(it)) },
        label = "Biography",
        placeholder = "Introduce yourself here",
        enabled = !uiState.isLoading,
        singleLine = false,
        minLines = 4
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        SignupTextField(
            value = uiState.budgetMin,
            onValueChange = { onEvent(SignupUiEvent.OnBudgetMinChanged(it)) },
            label = "Budget min",
            placeholder = "500",
            enabled = !uiState.isLoading,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
        SignupTextField(
            value = uiState.budgetMax,
            onValueChange = { onEvent(SignupUiEvent.OnBudgetMaxChanged(it)) },
            label = "Budget max",
            placeholder = "900",
            enabled = !uiState.isLoading,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    SignupTextField(
        value = uiState.preferredArea,
        onValueChange = { onEvent(SignupUiEvent.OnPreferredAreaChanged(it)) },
        label = "Preferred area",
        placeholder = "Downtown",
        enabled = !uiState.isLoading
    )
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = DeepMocha
    )
}

@Composable
private fun SignupChoiceChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = chipShape,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBronze,
                contentColor = Color.White
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(40.dp),
            shape = chipShape,
            enabled = enabled,
            border = BorderStroke(1.dp, LightBronze.copy(alpha = 0.45f)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = DustyTaupe
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SignupTagChip(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) LightBronze else Color.Transparent
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Color(0xFFF9EADB) else Color(0xFFE7DDD1),
            contentColor = if (selected) LightBronze else DustyTaupe,
            disabledContainerColor = Color(0xFFE7DDD1).copy(alpha = 0.6f),
            disabledContentColor = DustyTaupe.copy(alpha = 0.45f)
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 6.dp,
            vertical = 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            softWrap = false,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SignupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    error: String? = null,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepMocha
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) 50.dp else 100.dp),
            placeholder = {
                if (placeholder.isNotBlank()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DustyTaupe.copy(alpha = 0.55f)
                    )
                }
            },
            enabled = enabled,
            singleLine = singleLine,
            minLines = minLines,
            isError = error != null,
            shape = inputShape,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = DustyTaupe,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        error?.let {
            Spacer(modifier = Modifier.height(4.dp))
            ErrorText(text = it)
        }
    }
}

@Composable
private fun SignupPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    error: String? = null,
    enabled: Boolean
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = DeepMocha
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DustyTaupe.copy(alpha = 0.55f)
                )
            },
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    modifier = Modifier.clickable(enabled = enabled, onClick = onTogglePasswordVisibility),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = LightBronze,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            enabled = enabled,
            singleLine = true,
            isError = error != null,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = inputShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = DustyTaupe,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        error?.let {
            Spacer(modifier = Modifier.height(4.dp))
            ErrorText(text = it)
        }
    }
}

@Composable
private fun ErrorText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true, name = "Signup UI - Step 1")
@Composable
private fun SignupPreviewStep1() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(currentStep = 1),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup UI - Step 2")
@Composable
private fun SignupPreviewStep2() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 2,
                firstName = "Jane",
                lastName = "Doe",
                universityEmail = "jane@university.edu"
            ),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup UI - Step 3")
@Composable
private fun SignupPreviewStep3() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 3,
                placeType = HousingPlaceType.OWN_ROOM,
                monthlyBudget = "900",
                socialStratum = 3,
                wantsKitchen = true,
                wantsInternet = true
            ),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup UI - Step 4")
@Composable
private fun SignupPreviewStep4() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 4,
                sleepSchedule = SleepSchedule.NIGHT_OWL,
                cleanlinessLevel = CleanlinessLevel.MODERATE,
                noisePreference = NoisePreference.QUIET,
                smokes = false,
                hasPets = true
            ),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Signup UI - Step 5")
@Composable
private fun SignupPreviewStep5() {
    G46KotlinTheme {
        SignupContent(
            uiState = SignupUiState(
                currentStep = 5,
                bio = "Busco roomie tranquilo y ordenado.",
                budgetMin = "500",
                budgetMax = "900",
                preferredArea = "Chapinero"
            ),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}