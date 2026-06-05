package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.NameInputEvent
import dev.whysoezzy.meetings.compose.ui.auth.NameInputUiState
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
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

class NameInputViewModel(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(NameInputUiState())
    val uiState: StateFlow<NameInputUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: NameInputEvent) {
        when (event) {
            is NameInputEvent.FirstNameChanged -> {
                _uiState.update { it.copy(firstName = event.firstName) }
            }
            is NameInputEvent.LastNameChanged -> {
                _uiState.update { it.copy(lastName = event.lastName) }
            }
            is NameInputEvent.Save -> saveName()
            is NameInputEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun saveName() {
        val state = _uiState.value
        if (state.firstName.isBlank()) return

        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            updateUserProfileUseCase(
                user = User(
                    id = 0,
                    name = state.firstName,
                    surname = state.lastName,
                    email = "",
                    city = "",
                    avatar = "",
                    phone = "",
                    bio = "",
                ),
            )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
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
