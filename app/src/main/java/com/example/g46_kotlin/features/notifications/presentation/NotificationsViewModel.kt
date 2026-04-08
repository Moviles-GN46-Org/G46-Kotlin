package com.example.g46_kotlin.features.notifications.presentation

import androidx.lifecycle.ViewModel
import com.example.g46_kotlin.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadAllNotificationsUseCase
import com.example.g46_kotlin.features.notifications.domain.usecase.ReadNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val readNotificationsUseCase: ReadNotificationsUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState = _uiState.asStateFlow()
}