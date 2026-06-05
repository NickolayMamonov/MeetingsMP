package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.ui.auth.AuthNavEvent
import dev.whysoezzy.meetings.compose.ui.auth.NameInputEvent
import dev.whysoezzy.meetings.compose.ui.auth.NameInputUiState
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NameInputViewModel(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NameInputUiState())
    val uiState: StateFlow<NameInputUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent: SharedFlow<AuthNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: NameInputEvent) {
        when (event) {
            is NameInputEvent.FirstNameChanged -> {
                _uiState.value = _uiState.value.copy(firstName = event.firstName)
            }
            is NameInputEvent.LastNameChanged -> {
                _uiState.value = _uiState.value.copy(lastName = event.lastName)
            }
            is NameInputEvent.Save -> {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            try {
                val user = User(
                    id = 0L,
                    name = state.firstName,
                    surname = state.lastName,
                    email = "",
                    city = "",
                    avatar = "",
                    phone = "",
                    bio = "",
                )
                updateUserProfileUseCase(user)
                _navEvent.emit(AuthNavEvent.NavigateToMain)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }
}
