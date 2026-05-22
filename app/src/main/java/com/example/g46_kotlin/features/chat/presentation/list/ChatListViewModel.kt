package com.example.g46_kotlin.features.chat.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.g46_kotlin.core.domain.contract.NetworkMonitor
import com.example.g46_kotlin.core.utils.JwtUtils
import com.example.g46_kotlin.features.auth.data.local.TokenStorage
import com.example.g46_kotlin.features.chat.domain.model.Chat
import com.example.g46_kotlin.features.chat.domain.repository.ChatRepository
import com.example.g46_kotlin.features.chat.domain.usecase.ListChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val listChatsUseCase: ListChatsUseCase,
    private val chatRepository: ChatRepository,
    private val networkMonitor: NetworkMonitor,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState = _uiState.asStateFlow()

    private var pollingJob: Job? = null
    @Volatile private var currentUserId: String = ""

    init {
        observeConnectivity()
        loadChats()
    }

    fun onRetry() = loadChats()

    fun startPolling() {
        if (pollingJob?.isActive == true) return
        if (_uiState.value.isOffline) return
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(15_000)
                loadChats(silent = true)
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
                    val cached = chatRepository.getCachedChats()
                    if (cached.isNotEmpty()) {
                        _uiState.update { it.copy(chats = cached.map { c -> c.toUi(currentUserId) }) }
                    }
                } else if (wasOffline) {
                    loadChats(silent = true)
                    startPolling()
                }
            }
        }
    }

    private fun loadChats(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            if (currentUserId.isEmpty()) {
                currentUserId = tokenStorage.getAccessToken()
                    ?.let { JwtUtils.extractUserId(it) } ?: ""
            }
            runCatching { listChatsUseCase() }
                .onSuccess { chats ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            chats = chats.map { chat -> chat.toUi(currentUserId) },
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    if (!silent) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = error.message ?: "Error loading chats")
                        }
                    }
                }
        }
    }
}

private fun Chat.toUi(currentUserId: String): ChatUi {
    val other = participants.firstOrNull { it.id != currentUserId }
    val lastMsg = lastMessage
    return ChatUi(
        id = id,
        otherParticipantName = if (other != null) "${other.firstName} ${other.lastName}" else "Unknown",
        otherParticipantAvatar = other?.profilePictureUrl,
        propertyTitle = property?.title,
        lastMessageContent = lastMsg?.content,
        lastMessageTime = formatChatTime(lastMsg?.createdAt ?: updatedAt),
        lastMessageIsRead = lastMsg?.isRead ?: true,
        lastMessageIsFromMe = lastMsg?.sender?.id == currentUserId,
        status = status
    )
}

private fun formatChatTime(isoString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(isoString) ?: return ""
        val now = Calendar.getInstance()
        val msgCal = Calendar.getInstance().apply { time = date }
        val sameDay = now.get(Calendar.YEAR) == msgCal.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == msgCal.get(Calendar.DAY_OF_YEAR)
        val isYesterday = !sameDay && (now.timeInMillis - date.time) < 48 * 60 * 60 * 1000L
        when {
            sameDay -> SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
            isYesterday -> "Yesterday"
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
        }
    } catch (e: Exception) { "" }
}