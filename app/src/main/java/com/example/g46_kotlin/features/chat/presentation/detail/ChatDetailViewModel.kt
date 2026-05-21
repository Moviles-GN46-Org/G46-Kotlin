package com.example.g46_kotlin.features.chat.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.core.utils.JwtUtils
import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import com.example.g46_kotlin.features.chat.domain.model.Message
import com.example.g46_kotlin.features.chat.domain.model.MessageStatus
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import com.example.g46_kotlin.features.chat.domain.usecase.GetMessagesUseCase
import com.example.g46_kotlin.features.chat.domain.usecase.SendMessageUseCase
import com.example.g46_kotlin.core.domain.contract.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val chatRepository: ChatRepository,
    private val tokenStorage: TokenStorage,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {

    val chatId: String = checkNotNull(savedStateHandle["chatId"]) { "chatId is required" }

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    @Volatile private var currentUserId: String = ""

    init {
        viewModelScope.launch {
            currentUserId = tokenStorage.getAccessToken()
                ?.let { JwtUtils.extractUserId(it) } ?: ""

            val cached = chatRepository.getCachedChats().firstOrNull { it.id == chatId }
            if (cached != null) {
                val other = cached.participants.firstOrNull { it.id != currentUserId }
                _uiState.update {
                    it.copy(
                        chatName = other?.let { p -> "${p.firstName} ${p.lastName}" } ?: "",
                        chatAvatar = other?.profilePictureUrl,
                        propertyId = cached.propertyId,
                        propertyTitle = cached.property?.title,
                        propertyImageUrl = cached.property?.imageUrls?.firstOrNull()
                    )
                }
            }

            if (networkMonitor.isConnected.first()) {
                loadMessages()
            } else {
                val cachedMsgs = chatRepository.getCachedMessages(chatId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOffline = true,
                        messages = cachedMsgs.map { m -> m.toUi(currentUserId) }
                    )
                }
            }
        }
        observeConnectivity()
    }

    fun startPolling() {
        if (pollingJob?.isActive == true) return
        if (_uiState.value.isOffline) return
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(5_000)
                pollNewMessages()
            }
        }

    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                val wasOffline = _uiState.value.isOffline
                _uiState.update { it.copy(isOffline = !isConnected) }
                if (!isConnected) {
                    stopPolling()
                    if (currentUserId.isEmpty()) {
                        currentUserId = tokenStorage.getAccessToken()
                            ?.let { JwtUtils.extractUserId(it) } ?: ""
                    }
                    val cached = chatRepository.getCachedMessages(chatId)
                    if (cached.isNotEmpty()) {
                        _uiState.update { it.copy(messages = cached.map { m -> m.toUi(currentUserId) }) }
                    }
                } else if (wasOffline) {
                    pollNewMessages()
                    startPolling()
                }
            }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onSendMessage() {
        val content = _uiState.value.inputText.trim()
        if (content.isBlank() || _uiState.value.isSending || _uiState.value.isOffline) return
        _uiState.update { it.copy(inputText = "", isSending = true) }

        viewModelScope.launch {
            runCatching { sendMessageUseCase(chatId, content) }
                .onSuccess { message ->
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages + message.toUi(currentUserId),
                            isSending = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isSending = false, inputText = content) }
                }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getMessagesUseCase(chatId, after = null) }
                .onSuccess { messages ->
                    _uiState.update {
                        it.copy(isLoading = false, messages = messages.map { m -> m.toUi(currentUserId) })
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    private fun pollNewMessages() {
        viewModelScope.launch {
            val lastTimestamp = _uiState.value.messages
                .filter { it.status == MessageStatus.SENT }
                .lastOrNull()?.createdAt

            runCatching { getMessagesUseCase(chatId, after = lastTimestamp) }
                .onSuccess { serverMessages ->
                    val existingIds = _uiState.value.messages.map { it.id }.toSet()
                    val newMessages = serverMessages
                        .map { it.toUi(currentUserId) }
                        .filter { it.id !in existingIds }
                    if (newMessages.isNotEmpty()) {
                        _uiState.update { it.copy(messages = it.messages + newMessages) }
                    }
                }
        }
    }

    private fun Message.toUi(userId: String) = MessageUi(
        id = id,
        content = content,
        isFromMe = sender.id == userId,
        type = type,
        time = formatMessageTime(createdAt),
        createdAt = createdAt,
        status = status
    )
}

private fun formatMessageTime(isoString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(isoString) ?: return ""
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    } catch (e: Exception) { "" }
}