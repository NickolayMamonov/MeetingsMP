package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.communities.CommunitySubscribersEvent
import dev.whysoezzy.meetings.compose.ui.communities.CommunitySubscribersUiState
import dev.whysoezzy.meetings.domain.usecase.GetCommunitySubscribersUseCase
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

class CommunitySubscribersViewModel(
    private val getCommunitySubscribersUseCase: GetCommunitySubscribersUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(CommunitySubscribersUiState())
    val uiState: StateFlow<CommunitySubscribersUiState> = _uiState.asStateFlow()

    fun onEvent(event: CommunitySubscribersEvent) {
        when (event) {
            is CommunitySubscribersEvent.Load -> loadSubscribers(event.communityId)
            is CommunitySubscribersEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadSubscribers(communityId: Long) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getCommunitySubscribersUseCase(communityId)
                .onSuccess { subscribers ->
                    _uiState.update {
                        it.copy(isLoading = false, subscribers = subscribers)
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

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
