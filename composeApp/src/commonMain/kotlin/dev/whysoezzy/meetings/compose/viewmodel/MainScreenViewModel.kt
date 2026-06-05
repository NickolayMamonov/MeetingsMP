package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.models.UIKitAdBlock
import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitMainScreenData
import dev.whysoezzy.meetings.compose.models.UIKitMeeting
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.models.UIKitTag
import dev.whysoezzy.meetings.domain.usecase.GetAllTagsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetMainScreenDataUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for MainScreen.
 */
data class MainScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val heroMeeting: UIKitMeeting? = null,
    val nearestMeetings: List<UIKitMeeting> = emptyList(),
    val recommendedCommunities: List<UIKitCommunity> = emptyList(),
    val suggestedUsers: List<UIKitPerson> = emptyList(),
    val adBlocks: List<UIKitAdBlock> = emptyList(),
    val tags: List<UIKitTag> = emptyList(),
    val selectedTagId: Long? = null,
    val allMeetings: List<UIKitMeeting> = emptyList(),
    val eventsNextCursor: String? = null,
    val isRefreshing: Boolean = false,
)

/**
 * Events for MainScreenViewModel.
 */
sealed interface MainScreenEvent {
    data class TagSelected(val tagId: Long) : MainScreenEvent
    data object Refresh : MainScreenEvent
    data class LoadMore(val cursor: String) : MainScreenEvent
}

sealed interface MainScreenNavEvent {
    data class MeetingDetails(val meetingId: Long) : MainScreenNavEvent
    data class MeetingParticipants(val meetingId: Long) : MainScreenNavEvent
    data class CommunityDetails(val communityId: Long) : MainScreenNavEvent
}

class MainScreenViewModel(
    private val getMainScreenDataUseCase: GetMainScreenDataUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<MainScreenNavEvent>()
    val navEvent: SharedFlow<MainScreenNavEvent> = _navEvent.asSharedFlow()

    init {
        loadData()
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.TagSelected -> filterByTag(event.tagId)
            is MainScreenEvent.Refresh -> refresh()
            is MainScreenEvent.LoadMore -> loadMore(event.cursor)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val mainScreenDataResult = getMainScreenDataUseCase()
                val tagsResult = getAllTagsUseCase()

                mainScreenDataResult.onSuccess { data ->
                    val uiData = data.toUIKit()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        heroMeeting = uiData.heroMeeting,
                        nearestMeetings = uiData.nearestMeetings,
                        recommendedCommunities = uiData.recommendedCommunities,
                        suggestedUsers = uiData.suggestedUsers,
                        allMeetings = uiData.allMeetings,
                        eventsNextCursor = uiData.eventsNextCursor,
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load data",
                    )
                }

                tagsResult.onSuccess { tags ->
                    _uiState.value = _uiState.value.copy(
                        tags = tags.map { it.toUIKit() },
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

    private fun filterByTag(tagId: Long) {
        val newSelectedId = if (_uiState.value.selectedTagId == tagId) null else tagId
        _uiState.value = _uiState.value.copy(selectedTagId = newSelectedId)

        viewModelScope.launch {
            try {
                val tagsParam = newSelectedId?.toString()
                val result = getMainScreenDataUseCase(tags = tagsParam)
                result.onSuccess { data ->
                    val uiData = data.toUIKit()
                    _uiState.value = _uiState.value.copy(
                        heroMeeting = uiData.heroMeeting,
                        nearestMeetings = uiData.nearestMeetings,
                        recommendedCommunities = uiData.recommendedCommunities,
                        allMeetings = uiData.allMeetings,
                    )
                }
            } catch (_: Exception) { }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            loadData()
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun loadMore(cursor: String) {
        viewModelScope.launch {
            try {
                val result = getMainScreenDataUseCase(cursor = cursor)
                result.onSuccess { data ->
                    val uiData = data.toUIKit()
                    _uiState.value = _uiState.value.copy(
                        allMeetings = _uiState.value.allMeetings + uiData.allMeetings,
                        eventsNextCursor = uiData.eventsNextCursor,
                    )
                }
            } catch (_: Exception) { }
        }
    }
}
