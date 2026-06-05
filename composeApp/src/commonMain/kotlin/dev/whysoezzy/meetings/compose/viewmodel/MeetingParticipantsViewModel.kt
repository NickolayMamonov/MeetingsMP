package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingParticipantsEvent
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingParticipantsUiState
import dev.whysoezzy.meetings.domain.usecase.GetMeetingParticipantsUseCase
import dev.whysoezzy.meetingssdk.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MeetingParticipantsViewModel(
    private val getMeetingParticipantsUseCase: GetMeetingParticipantsUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(MeetingParticipantsUiState())
    val uiState: StateFlow<MeetingParticipantsUiState> = _uiState.asStateFlow()

    fun onEvent(event: MeetingParticipantsEvent) {
        when (event) {
            is MeetingParticipantsEvent.Load -> loadParticipants(event.meetingId)
            is MeetingParticipantsEvent.LoadMore -> loadMore(event.meetingId, event.cursor)
            is MeetingParticipantsEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadParticipants(meetingId: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMeetingParticipantsUseCase(meetingId)
                .onSuccess { participants ->
                    _uiState.update {
                        it.copy(isLoading = false, participants = participants)
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

    private fun loadMore(meetingId: String, cursor: String) {
        scope.launch {
            getMeetingParticipantsUseCase(meetingId, cursor = cursor)
                .onSuccess { participants ->
                    _uiState.update {
                        it.copy(
                            participants = it.participants + participants,
                            nextCursor = null,
                        )
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
