package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.domain.usecase.GetMeetingParticipantsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for MeetingParticipantsScreen.
 */
data class MeetingParticipantsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val participants: List<UIKitPerson> = emptyList(),
)

/**
 * Events for MeetingParticipantsViewModel.
 */
sealed interface MeetingParticipantsEvent

class MeetingParticipantsViewModel(
    private val meetingId: String,
    private val getMeetingParticipantsUseCase: GetMeetingParticipantsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeetingParticipantsUiState())
    val uiState: StateFlow<MeetingParticipantsUiState> = _uiState.asStateFlow()

    init {
        loadParticipants()
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getMeetingParticipantsUseCase(meetingId)
                result.onSuccess { persons ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        participants = persons.map { it.toUIKit() },
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load participants",
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
}
