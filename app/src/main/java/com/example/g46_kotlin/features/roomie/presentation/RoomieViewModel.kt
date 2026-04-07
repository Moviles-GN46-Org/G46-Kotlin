package com.example.g46_kotlin.features.roomie.presentation

import com.example.g46_kotlin.features.roomie.domain.usecase.GetRecommendedRoomiesUseCase
import com.example.g46_kotlin.features.roomie.domain.usecase.SubmitRoomieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.features.roomie.domain.model.Roomie
import com.example.g46_kotlin.features.roomie.presentation.components.RoomieCardUi
import com.example.g46_kotlin.features.roomie.presentation.mapper.mapMultiOptionPreferences
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.invoke


@HiltViewModel
class RoomieViewModel @Inject constructor(
    private val getRecommendedRoomiesUseCase: GetRecommendedRoomiesUseCase,
    private val submitRoomieUseCase: SubmitRoomieUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(RoomieUiState())
    val uiState: StateFlow<RoomieUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<RoomieEffect>()
    val effects: SharedFlow<RoomieEffect> = _effects.asSharedFlow()

    //historial local
    private val decisionHistory = ArrayDeque<DecidedCard>()

    fun onEvent(event: RoomieUiEvent) {
        when (event) {
            RoomieUiEvent.OnScreenStarted -> onScreenStarted()
            RoomieUiEvent.OnRefresh -> onRefresh()
            is RoomieUiEvent.OnLike -> onSwipe(event.roomieId, liked = true)
            is RoomieUiEvent.OnPass -> onSwipe(event.roomieId, liked = false)
            is RoomieUiEvent.OnCardClicked -> onCardClicked(event.roomieId)
            RoomieUiEvent.OnRetryAfterError -> onRetryAfterError()
            RoomieUiEvent.OnUndo -> onUndo()
        }
    }
    private fun onScreenStarted() {
        if (_uiState.value.queue.isEmpty()) loadQueue()
    }

    private fun onRefresh() = loadQueue(force = true)

    private fun loadQueue(force: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, endReached = false)
            }

            runCatching { getRecommendedRoomiesUseCase.invoke() }
                .onSuccess { roomies ->
                    val cards = roomies.map(::toCardUi)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            queue = cards,
                            current = cards.firstOrNull(),
                            totalLoaded = cards.size,
                            seenCount = 0,
                            endReached = cards.isEmpty(),
                            canUndo = decisionHistory.isNotEmpty()
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Error cargando roomies")
                    }
                    _effects.emit(RoomieEffect.ShowMessage("No se pudieron cargar recomendaciones"))
                }
        }
    }

    private fun onSwipe(roomieId: String, liked: Boolean) {
        val state = _uiState.value
        val current = state.current ?: return
        if (state.isSubmittingDecision) return

        // avance optimista
        val remaining = state.queue.drop(1)
        decisionHistory.addFirst(DecidedCard(current, liked))

        _uiState.update {
            it.copy(
                queue = remaining,
                current = remaining.firstOrNull(),
                seenCount = it.seenCount + 1,
                isSubmittingDecision = true,
                canUndo = true,
                endReached = remaining.isEmpty()
            )
        }

        viewModelScope.launch {
            runCatching { submitRoomieUseCase(roomieId, liked) }
                .onSuccess { result ->
                    if (!result.swiped) {
                        _effects.emit(RoomieEffect.ShowMessage("No se pudo registrar el swipe"))
                    } else if (result.matched) {
                        _effects.emit(RoomieEffect.ShowMessage("It's a match!"))
                    }
                }
                .onFailure {
                    _effects.emit(RoomieEffect.ShowMessage("Error enviando swipe"))
                }

            _uiState.update { it.copy(isSubmittingDecision = false) }
        }
    }

    private fun onCardClicked(roomieId: String) {
        viewModelScope.launch {
            _effects.emit(RoomieEffect.NavigateToRoomieDetail(roomieId))
        }
    }

    private fun onRetryAfterError() = loadQueue(force = true)

    private fun onUndo() {
        val last = decisionHistory.removeFirstOrNull() ?: return
        val currentQueue = _uiState.value.queue
        val restored = listOf(last.card) + currentQueue
        _uiState.update {
            it.copy(
                queue = restored,
                current = restored.firstOrNull(),
                seenCount = (it.seenCount - 1).coerceAtLeast(0),
                canUndo = decisionHistory.isNotEmpty(),
                endReached = false
            )
        }
    }

    private data class DecidedCard(
        val card: RoomieCardUi,
        val liked: Boolean
    )

    //TODO: modificar cuando arreglemos backend
    private fun toCardUi(roomie: Roomie): RoomieCardUi {
        val prefs = mapMultiOptionPreferences(
            sleepSchedule = roomie.sleepSchedule.toString(),
            cleanlinessLevel = roomie.cleanlinessLevel.toString(),
            noisePreference = roomie.noisePreference.toString()
        )

        return RoomieCardUi(
            name = "${roomie.firstName} ${roomie.lastName}",
            age = roomie.age,
            matchRate = roomie.age,
            budget = roomie.budgetMax,
            job = roomie.job,
            university = roomie.university,
            about = roomie.bio,
            habitsPreferences = prefs,
            profilePicture = roomie.profilePictureUrl
        )
    }
}