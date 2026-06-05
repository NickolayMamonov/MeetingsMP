package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingDetailsEvent
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingDetailsUiState
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingsNavEvent
import dev.whysoezzy.meetings.domain.usecase.GetMeetingByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.JoinMeetingUseCase
import dev.whysoezzy.meetings.domain.usecase.LeaveMeetingUseCase
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

class MeetingDetailsViewModel(
    private val getMeetingByIdUseCase: GetMeetingByIdUseCase,
    private val joinMeetingUseCase: JoinMeetingUseCase,
    private val leaveMeetingUseCase: LeaveMeetingUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(MeetingDetailsUiState())
    val uiState: StateFlow<MeetingDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<MeetingsNavEvent>()
    val navEvent: SharedFlow<MeetingsNavEvent> = _navEvent.asSharedFlow()

    private var currentMeetingId: String = ""

    fun onEvent(event: MeetingDetailsEvent) {
        when (event) {
            is MeetingDetailsEvent.Load -> loadMeeting(event.meetingId)
            is MeetingDetailsEvent.Join -> join()
            is MeetingDetailsEvent.Leave -> leave()
            is MeetingDetailsEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadMeeting(meetingId: String) {
        currentMeetingId = meetingId
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMeetingByIdUseCase(meetingId)
                .onSuccess { meeting ->
                    _uiState.update {
                        it.copy(isLoading = false, meeting = meeting)
                    }
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

    private fun join() {
        scope.launch {
            _uiState.update { it.copy(isJoining = true, error = null) }

            joinMeetingUseCase(currentMeetingId)
                .onSuccess {
                    _uiState.update { state ->
                        val updatedMeeting = state.meeting?.copy(isUserInParticipants = true)
                        state.copy(isJoining = false, meeting = updatedMeeting)
                    }
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isJoining = false, error = errorType)
                    }
                }
        }
    }

    private fun leave() {
        scope.launch {
            _uiState.update { it.copy(isLeaving = true, error = null) }

            leaveMeetingUseCase(currentMeetingId)
                .onSuccess {
                    _uiState.update { state ->
                        val updatedMeeting = state.meeting?.copy(isUserInParticipants = false)
                        state.copy(isLeaving = false, meeting = updatedMeeting)
                    }
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isLeaving = false, error = errorType)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
