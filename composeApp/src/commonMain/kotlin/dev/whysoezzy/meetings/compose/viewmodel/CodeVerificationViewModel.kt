package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.CodeVerificationEvent
import dev.whysoezzy.meetings.compose.ui.auth.CodeVerificationUiState
import dev.whysoezzy.meetings.domain.usecase.VerifyOtpUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CodeVerificationViewModel(
    private val phone: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CodeVerificationUiState())
    val uiState: StateFlow<CodeVerificationUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: CodeVerificationEvent) {
        when (event) {
            is CodeVerificationEvent.CodeChanged -> {
                _uiState.value = _uiState.value.copy(code = event.code)
            }
            is CodeVerificationEvent.VerifyCode -> {
                verifyCode()
            }
        }
    }

    private fun verifyCode() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            try {
                val result = verifyOtpUseCase(phone, state.code)
                val authResult = result.getOrThrow()
                if (authResult.isNewUser) {
                    _navEvent.emit(AuthNavEvent.NavigateToNameInput)
                } else {
                    _navEvent.emit(AuthNavEvent.NavigateToMain)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }
}
