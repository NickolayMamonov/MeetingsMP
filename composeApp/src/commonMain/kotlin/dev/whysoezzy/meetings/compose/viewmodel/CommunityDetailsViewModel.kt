package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.communities.CommunitiesNavEvent
import dev.whysoezzy.meetings.compose.ui.communities.CommunityDetailsEvent
import dev.whysoezzy.meetings.compose.ui.communities.CommunityDetailsUiState
import dev.whysoezzy.meetings.domain.usecase.GetCommunityByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetCommunityMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.SubscribeToCommunityUseCase
import dev.whysoezzy.meetings.domain.usecase.UnsubscribeFromCommunityUseCase
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

class CommunityDetailsViewModel(
    private val getCommunityByIdUseCase: GetCommunityByIdUseCase,
    private val getCommunityMeetingsUseCase: GetCommunityMeetingsUseCase,
    private val subscribeToCommunityUseCase: SubscribeToCommunityUseCase,
    private val unsubscribeFromCommunityUseCase: UnsubscribeFromCommunityUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(CommunityDetailsUiState())
    val uiState: StateFlow<CommunityDetailsUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<CommunitiesNavEvent>()
    val navEvent: SharedFlow<CommunitiesNavEvent> = _navEvent.asSharedFlow()

    private var currentCommunityId: Long = 0L

    fun onEvent(event: CommunityDetailsEvent) {
        when (event) {
            is CommunityDetailsEvent.Load -> loadCommunity(event.communityId)
            is CommunityDetailsEvent.Subscribe -> subscribe()
            is CommunityDetailsEvent.Unsubscribe -> unsubscribe()
            is CommunityDetailsEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadCommunity(communityId: Long) {
        currentCommunityId = communityId
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val communityResult = getCommunityByIdUseCase(communityId)
            val meetingsResult = getCommunityMeetingsUseCase(communityId)

            val community = communityResult.getOrNull()
            val meetings = meetingsResult.getOrNull() ?: emptyList()

            if (community != null) {
                _uiState.update {
                    it.copy(isLoading = false, community = community, meetings = meetings)
                }
            } else {
                val error = communityResult.exceptionOrNull()
                val errorType = (error as? ApiException)
                    ?.toErrorType() ?: ErrorType.Unknown
                _uiState.update {
                    it.copy(isLoading = false, error = errorType)
                }
            }
        }
    }

    private fun subscribe() {
        scope.launch {
            _uiState.update { it.copy(isSubscribing = true, error = null) }

            subscribeToCommunityUseCase(currentCommunityId)
                .onSuccess {
                    _uiState.update { state ->
                        val community = state.community ?: return@update state
                        val updatedCommunity = community.copy(
                            isSubscribed = true,
                            subscribersCount = community.subscribersCount + 1,
                        )
                        state.copy(isSubscribing = false, community = updatedCommunity)
                    }
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isSubscribing = false, error = errorType)
                    }
                }
        }
    }

    private fun unsubscribe() {
        scope.launch {
            _uiState.update { it.copy(isSubscribing = true, error = null) }

            unsubscribeFromCommunityUseCase(currentCommunityId)
                .onSuccess {
                    _uiState.update { state ->
                        val community = state.community ?: return@update state
                        val updatedCommunity = community.copy(
                            isSubscribed = false,
                            subscribersCount = (community.subscribersCount - 1).coerceAtLeast(0),
                        )
                        state.copy(isSubscribing = false, community = updatedCommunity)
                    }
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isSubscribing = false, error = errorType)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
