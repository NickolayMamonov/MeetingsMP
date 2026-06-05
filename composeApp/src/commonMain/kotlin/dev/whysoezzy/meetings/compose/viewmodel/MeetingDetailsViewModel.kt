package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.models.UIKitMeeting
import dev.whysoezzy.meetings.domain.usecase.GetMeetingByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.JoinMeetingUseCase
import dev.whysoezzy.meetings.domain.usecase.LeaveMeetingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for MeetingDetailsScreen.
 */
data class MeetingDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val meeting: UIKitMeeting? = null,
    val isJoining: Boolean = false,
)

/**
 * Events for MeetingDetailsViewModel.
 */
sealed interface MeetingDetailsEvent {
    data object JoinMeeting : MeetingDetailsEvent
    data object LeaveMeeting : MeetingDetailsEvent
}

sealed interface MeetingDetailsNavEvent {
    data class Participants(val meetingId: Long) : MeetingDetailsNavEvent
    data object NavigateBack : MeetingDetailsNavEvent
}

class MeetingDetailsViewModel(
    private val meetingId: String,
    private val getMeetingByIdUseCase: GetMeetingByIdUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase,
    private val leaveMeetingUseCase: LeaveMeetingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeetingDetailsUiState())
    val uiState: StateFlow<MeetingDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<MeetingDetailsNavEvent>()
    val navEvent: SharedFlow<MeetingDetailsNavEvent> = _navEvent.asSharedFlow()

    init {
        loadMeeting()
    }

    fun onEvent(event: MeetingDetailsEvent) {
        when (event) {
            is MeetingDetailsEvent.JoinMeeting -> join()
            is MeetingDetailsEvent.LeaveMeeting -> leave()
        }
    }

    private fun loadMeeting() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getMeetingByIdUseCase(meetingId)
                result.onSuccess { meeting ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        meeting = meeting.toUIKit(),
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load meeting",
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

    private fun join() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isJoining = true)
            try {
                val result = joinMeetingUseCase(meetingId)
                result.onSuccess {
                    loadMeeting()
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isJoining = false,
                        error = e.message ?: "Failed to join meeting",
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isJoining = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }

    private fun leave() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isJoining = true)
            try {
                val result = leaveMeetingUseCase(meetingId)
                result.onSuccess {
                    loadMeeting()
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isJoining = false,
                        error = e.message ?: "Failed to leave meeting",
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isJoining = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }
}
