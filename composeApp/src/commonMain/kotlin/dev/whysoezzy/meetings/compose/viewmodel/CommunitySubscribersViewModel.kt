package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.domain.usecase.GetCommunitySubscribersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for CommunitySubscribersScreen.
 */
data class CommunitySubscribersUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val subscribers: List<UIKitPerson> = emptyList(),
)

/**
 * Events for CommunitySubscribersViewModel.
 */
sealed interface CommunitySubscribersEvent

class CommunitySubscribersViewModel(
    private val communityId: Long,
    private val getCommunitySubscribersUseCase: GetCommunitySubscribersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunitySubscribersUiState())
    val uiState: StateFlow<CommunitySubscribersUiState> = _uiState.asStateFlow()

    init {
        loadSubscribers()
    }

    private fun loadSubscribers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getCommunitySubscribersUseCase(communityId)
                result.onSuccess { persons ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        subscribers = persons.map { it.toUIKit() },
                    )
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load subscribers",
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
