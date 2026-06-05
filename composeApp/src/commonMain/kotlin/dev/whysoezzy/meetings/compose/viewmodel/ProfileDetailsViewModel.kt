package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.profile.ProfileDetailsEvent
import dev.whysoezzy.meetings.compose.ui.profile.ProfileDetailsUiState
import dev.whysoezzy.meetings.compose.ui.profile.ProfileNavEvent
import dev.whysoezzy.meetings.domain.usecase.GetCurrentUserUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserCommunitiesUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.LogoutUseCase
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

class ProfileDetailsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getUserMeetingsUseCase: GetUserMeetingsUseCase,
    private val getUserCommunitiesUseCase: GetUserCommunitiesUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(ProfileDetailsUiState())
    val uiState: StateFlow<ProfileDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<ProfileNavEvent>()
    val navEvent: SharedFlow<ProfileNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: ProfileDetailsEvent) {
        when (event) {
            is ProfileDetailsEvent.Load -> loadProfile(event.userId)
            is ProfileDetailsEvent.LoadCurrentUser -> loadCurrentUser()
            is ProfileDetailsEvent.Logout -> logout()
            is ProfileDetailsEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadCurrentUser() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getCurrentUserUseCase()
                .onSuccess { user ->
                    loadUserData(user.id.toString())
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

    private fun loadProfile(userId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getUserByIdUseCase(userId)
                .onSuccess { user ->
                    loadUserData(user.id.toString())
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

    private suspend fun loadUserData(userId: String) {
        val userResult = getCurrentUserUseCase()
        val meetingsResult = getUserMeetingsUseCase(userId)
        val communitiesResult = getUserCommunitiesUseCase(userId)

        val user = userResult.getOrNull()
        val meetings = meetingsResult.getOrNull() ?: emptyList()
        val communities = communitiesResult.getOrNull() ?: emptyList()

        if (user != null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    user = user,
                    userMeetings = meetings,
                    userCommunities = communities,
                )
            }
        } else {
            val error = userResult.exceptionOrNull()
            val errorType = (error as? ApiException)
                ?.toErrorType() ?: ErrorType.Unknown
            _uiState.update {
                it.copy(isLoading = false, error = errorType)
            }
        }
    }

    private fun logout() {
        scope.launch {
            logoutUseCase()
            _navEvent.emit(ProfileNavEvent.NavigateToLogin)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
