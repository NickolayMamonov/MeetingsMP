package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputEvent
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputUiState
import dev.whysoezzy.meetings.domain.usecase.SendOtpUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhoneInputViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhoneInputUiState())
    val uiState: StateFlow<PhoneInputUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: PhoneInputEvent) {
        when (event) {
            is PhoneInputEvent.PhoneChanged -> {
                _uiState.value = _uiState.value.copy(phone = event.phone)
            }
            is PhoneInputEvent.FirstNameChanged -> {
                _uiState.value = _uiState.value.copy(firstName = event.firstName)
            }
            is PhoneInputEvent.SendCode -> {
                sendCode()
            }
        }
    }

    private fun sendCode() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            try {
                sendOtpUseCase(state.phone, state.firstName)
                _navEvent.emit(AuthNavEvent.NavigateToCodeVerification)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }
}
