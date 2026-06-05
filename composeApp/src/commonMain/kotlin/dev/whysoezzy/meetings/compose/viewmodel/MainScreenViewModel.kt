package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.meetings.MainScreenEvent
import dev.whysoezzy.meetings.compose.ui.meetings.MainScreenUiState
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingsNavEvent
import dev.whysoezzy.meetings.domain.usecase.GetAllMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetMainScreenDataUseCase
import dev.whysoezzy.meetings.domain.usecase.GetPopularMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetRecommendedCommunitiesUseCase
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

class MainScreenViewModel(
    private val getMainScreenDataUseCase: GetMainScreenDataUseCase,
    private val getPopularMeetingsUseCase: GetPopularMeetingsUseCase,
    private val getAllMeetingsUseCase: GetAllMeetingsUseCase,
    private val getRecommendedCommunitiesUseCase: GetRecommendedCommunitiesUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<MeetingsNavEvent>()
    val navEvent: SharedFlow<MeetingsNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Load -> loadMainScreen()
            is MainScreenEvent.Refresh -> refresh()
            is MainScreenEvent.SelectTag -> selectTag(event.tagId)
            is MainScreenEvent.ClearError -> _uiState.update { it.copy(error = null) }
            is MainScreenEvent.LoadMore -> loadMore(event.cursor)
        }
    }

    private fun loadMainScreen() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMainScreenDataUseCase()
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(isLoading = false, data = data)
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

    private fun refresh() {
        scope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            getMainScreenDataUseCase()
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(isRefreshing = false, data = data)
                    }
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isRefreshing = false, error = errorType)
                    }
                }
        }
    }

    private fun selectTag(tagId: Long) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getAllMeetingsUseCase(tags = tagId.toString())
                .onSuccess { meetings ->
                    _uiState.update { state ->
                        val currentData = state.data
                        if (currentData != null) {
                            state.copy(
                                isLoading = false,
                                data = currentData.copy(allMeetings = meetings),
                            )
                        } else {
                            state.copy(isLoading = false)
                        }
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun loadMore(cursor: String) {
        scope.launch {
            getMainScreenDataUseCase(cursor = cursor)
                .onSuccess { data ->
                    _uiState.update { state ->
                        val currentData = state.data
                        if (currentData != null) {
                            state.copy(
                                data = currentData.copy(
                                    allMeetings = currentData.allMeetings + data.allMeetings,
                                    eventsNextCursor = data.eventsNextCursor,
                                ),
                            )
                        } else {
                            state.copy(data = data)
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
