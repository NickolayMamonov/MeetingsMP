package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.mapper.toUIKitInfo
import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.domain.usecase.GetCommunityByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetCommunityMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.SubscribeToCommunityUseCase
import dev.whysoezzy.meetings.domain.usecase.UnsubscribeFromCommunityUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for CommunityDetailsScreen.
 */
data class CommunityDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val community: UIKitCommunity? = null,
    val meetings: List<UIKitMeetingInfo> = emptyList(),
    val isSubscribing: Boolean = false,
)

/**
 * Events for CommunityDetailsViewModel.
 */
sealed interface CommunityDetailsEvent {
    data object ToggleSubscription : CommunityDetailsEvent
}

sealed interface CommunityDetailsNavEvent {
    data class Subscribers(val communityId: Long) : CommunityDetailsNavEvent
    data object NavigateBack : CommunityDetailsNavEvent
}

class CommunityDetailsViewModel(
    private val communityId: Long,
    private val getCommunityByIdUseCase: GetCommunityByIdUseCase,
    private val getCommunityMeetingsUseCase: GetCommunityMeetingsUseCase,
    private val subscribeToCommunityUseCase: SubscribeToCommunityUseCase,
    private val unsubscribeFromCommunityUseCase: UnsubscribeFromCommunityUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityDetailsUiState())
    val uiState: StateFlow<CommunityDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<CommunityDetailsNavEvent>()
    val navEvent: SharedFlow<CommunityDetailsNavEvent> = _navEvent.asSharedFlow()

    init {
        loadCommunity()
    }

    fun onEvent(event: CommunityDetailsEvent) {
        when (event) {
            is CommunityDetailsEvent.ToggleSubscription -> toggleSubscription()
        }
    }

    private fun loadCommunity() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val communityResult = getCommunityByIdUseCase(communityId)
                val meetingsResult = getCommunityMeetingsUseCase(communityId)

                communityResult.onSuccess { community ->
                    _uiState.value = _uiState.value.copy(
                        community = community.toUIKit(),
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load community",
                    )
                }

                meetingsResult.onSuccess { meetings ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        meetings = meetings.map { it.toUIKitInfo() },
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

    private fun toggleSubscription() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubscribing = true)
            val currentCommunity = _uiState.value.community ?: return@launch
            try {
                val result = if (currentCommunity.isSubscribed) {
                    unsubscribeFromCommunityUseCase(communityId)
                } else {
                    subscribeToCommunityUseCase(communityId)
                }
                result.onSuccess {
                    loadCommunity()
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSubscribing = false,
                        error = e.message,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubscribing = false,
                    error = e.message,
                )
            }
        }
    }
}
