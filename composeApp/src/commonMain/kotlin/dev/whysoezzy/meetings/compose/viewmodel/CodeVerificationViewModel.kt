package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.CodeVerificationEvent
import dev.whysoezzy.meetings.compose.ui.auth.CodeVerificationUiState
import dev.whysoezzy.meetings.domain.usecase.VerifyOtpUseCase
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

class CodeVerificationViewModel(
    private val phone: String,
    private val verifyOtpUseCase: VerifyOtpUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(CodeVerificationUiState())
    val uiState: StateFlow<CodeVerificationUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: CodeVerificationEvent) {
        when (event) {
            is CodeVerificationEvent.CodeChanged -> {
                _uiState.update {
                    it.copy(
                        code = event.code,
                        isValid = event.code.length >= 4,
                    )
                }
            }
            is CodeVerificationEvent.VerifyCode -> verifyCode()
            is CodeVerificationEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun verifyCode() {
        val code = _uiState.value.code
        if (code.isBlank()) return

        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            verifyOtpUseCase(phone, code)
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false, isVerified = true)
                    }
                    _navEvent.emit(AuthNavEvent.NavigateToMain)
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
