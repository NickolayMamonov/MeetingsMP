package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.models.UIKitTag
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.usecase.GetCurrentUserUseCase
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
import dev.whysoezzy.meetings.domain.usecase.GetAllTagsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for ProfileEditScreen.
 */
data class ProfileEditUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val surname: String = "",
    val city: String = "",
    val bio: String = "",
    val avatar: String = "",
    val showCommunities: Boolean = true,
    val showMeetings: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val availableTags: List<UIKitTag> = emptyList(),
    val selectedTagIds: Set<Long> = emptySet(),
)

/**
 * Events for ProfileEditViewModel.
 */
sealed interface ProfileEditEvent {
    data class NameChanged(val name: String) : ProfileEditEvent
    data class SurnameChanged(val surname: String) : ProfileEditEvent
    data class CityChanged(val city: String) : ProfileEditEvent
    data class BioChanged(val bio: String) : ProfileEditEvent
    data class ShowCommunitiesToggled(val show: Boolean) : ProfileEditEvent
    data class ShowMeetingsToggled(val show: Boolean) : ProfileEditEvent
    data class NotificationsToggled(val enabled: Boolean) : ProfileEditEvent
    data class TagToggled(val tagId: Long) : ProfileEditEvent
    data object Save : ProfileEditEvent
}

sealed interface ProfileEditNavEvent {
    data object NavigateBack : ProfileEditNavEvent
}

class ProfileEditViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<ProfileEditNavEvent>()
    val navEvent: SharedFlow<ProfileEditNavEvent> = _navEvent.asSharedFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileEditEvent) {
        when (event) {
            is ProfileEditEvent.NameChanged ->
                _uiState.value = _uiState.value.copy(name = event.name)
            is ProfileEditEvent.SurnameChanged ->
                _uiState.value = _uiState.value.copy(surname = event.surname)
            is ProfileEditEvent.CityChanged ->
                _uiState.value = _uiState.value.copy(city = event.city)
            is ProfileEditEvent.BioChanged ->
                _uiState.value = _uiState.value.copy(bio = event.bio)
            is ProfileEditEvent.ShowCommunitiesToggled ->
                _uiState.value = _uiState.value.copy(showCommunities = event.show)
            is ProfileEditEvent.ShowMeetingsToggled ->
                _uiState.value = _uiState.value.copy(showMeetings = event.show)
            is ProfileEditEvent.NotificationsToggled ->
                _uiState.value = _uiState.value.copy(notificationsEnabled = event.enabled)
            is ProfileEditEvent.TagToggled -> {
                val current = _uiState.value.selectedTagIds
                val updated = if (event.tagId in current) current - event.tagId else current + event.tagId
                _uiState.value = _uiState.value.copy(selectedTagIds = updated)
            }
            is ProfileEditEvent.Save -> save()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val userResult = getCurrentUserUseCase()
                val tagsResult = getAllTagsUseCase()

                userResult.onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        name = user.name,
                        surname = user.surname,
                        city = user.city,
                        bio = user.bio,
                        avatar = user.avatar,
                        showCommunities = user.showCommunities,
                        showMeetings = user.showMeetings,
                        notificationsEnabled = user.notificationsEnabled,
                        selectedTagIds = user.interests.map { it.id }.toSet(),
                    )
                }

                tagsResult.onSuccess { tags ->
                    _uiState.value = _uiState.value.copy(
                        availableTags = tags.map { it.toUIKit() },
                    )
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null)
            try {
                val updatedUser = User(
                    id = 0L,
                    name = state.name,
                    surname = state.surname,
                    email = "",
                    city = state.city,
                    avatar = state.avatar,
                    phone = "",
                    bio = state.bio,
                    showCommunities = state.showCommunities,
                    showMeetings = state.showMeetings,
                    notificationsEnabled = state.notificationsEnabled,
                )
                val result = updateUserProfileUseCase(
                    user = updatedUser,
                    interestIds = state.selectedTagIds.toList(),
                )
                result.onSuccess {
                    _navEvent.emit(ProfileEditNavEvent.NavigateBack)
                }.onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save profile",
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Unknown error",
                )
            }
        }
    }
}
