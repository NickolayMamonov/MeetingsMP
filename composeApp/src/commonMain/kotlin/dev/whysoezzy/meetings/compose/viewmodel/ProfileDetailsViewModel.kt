package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.mapper.toUIKitInfo
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.models.UIKitUser
import dev.whysoezzy.meetings.domain.usecase.GetCurrentUserUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserCommunitiesUseCase
import dev.whysoezzy.meetings.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for ProfileDetailsScreen.
 */
data class ProfileDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val user: UIKitUser? = null,
    val meetings: List<UIKitMeetingInfo> = emptyList(),
    val communities: List<UIKitCommunityInfo> = emptyList(),
    val isLoggingOut: Boolean = false,
)

/**
 * Events for ProfileDetailsViewModel.
 */
sealed interface ProfileDetailsEvent {
    data object Logout : ProfileDetailsEvent
}

sealed interface ProfileDetailsNavEvent {
    data object NavigateToEdit : ProfileDetailsNavEvent
    data object NavigateToAuth : ProfileDetailsNavEvent
}

class ProfileDetailsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserMeetingsUseCase: GetUserMeetingsUseCase,
    private val getUserCommunitiesUseCase: GetUserCommunitiesUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailsUiState())
    val uiState: StateFlow<ProfileDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<ProfileDetailsNavEvent>()
    val navEvent: SharedFlow<ProfileDetailsNavEvent> = _navEvent.asSharedFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileDetailsEvent) {
        when (event) {
            is ProfileDetailsEvent.Logout -> logout()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val userResult = getCurrentUserUseCase()
                userResult.onSuccess { user ->
                    val userMeetingsResult = getUserMeetingsUseCase(user.id.toString())
                    val userCommunitiesResult = getUserCommunitiesUseCase(user.id.toString())

                    _uiState.value = _uiState.value.copy(
                        user = user.toUIKit(),
                    )

                    userMeetingsResult.onSuccess { meetings ->
                        _uiState.value = _uiState.value.copy(
                            meetings = meetings.map { it.toUIKitInfo() },
                        )
                    }

                    userCommunitiesResult.onSuccess { communities ->
                        _uiState.value = _uiState.value.copy(
                            communities = communities.map { it.toUIKitInfo() },
                        )
                    }

                    _uiState.value = _uiState.value.copy(isLoading = false)
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load profile",
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoggingOut = true)
            try {
                logoutUseCase()
                _navEvent.emit(ProfileDetailsNavEvent.NavigateToAuth)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoggingOut = false,
                    error = e.message,
                )
            }
        }
    }
}
