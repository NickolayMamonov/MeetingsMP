package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputEvent
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputUiState
import dev.whysoezzy.meetings.domain.usecase.SendOtpUseCase
import dev.whysoezzy.meetingssdk.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneInputViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(PhoneInputUiState())
    val uiState: StateFlow<PhoneInputUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: PhoneInputEvent) {
        when (event) {
            is PhoneInputEvent.PhoneChanged -> {
                _uiState.update { it.copy(phone = event.phone) }
            }
            is PhoneInputEvent.SendCode -> sendCode()
            is PhoneInputEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun sendCode() {
        val state = _uiState.value
        if (state.phone.isBlank()) return

        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            sendOtpUseCase(state.phone)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            codeSent = true,
                        )
                    }
                    _navEvent.emit(AuthNavEvent.NavigateToCodeVerification)
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isLoading = false, error = errorType)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
