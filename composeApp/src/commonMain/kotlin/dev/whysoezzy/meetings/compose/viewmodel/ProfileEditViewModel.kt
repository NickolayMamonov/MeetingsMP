package dev.whysoezzy.meetings.compose.viewmodel

import androidx.lifecycle.ViewModel
import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.common.error.toErrorType
import dev.whysoezzy.meetings.compose.ui.profile.ProfileEditEvent
import dev.whysoezzy.meetings.compose.ui.profile.ProfileEditUiState
import dev.whysoezzy.meetings.compose.ui.profile.ProfileNavEvent
import dev.whysoezzy.meetings.domain.usecase.GetCurrentUserUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
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

class ProfileEditViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<ProfileNavEvent>()
    val navEvent: SharedFlow<ProfileNavEvent> = _navEvent.asSharedFlow()

    fun onEvent(event: ProfileEditEvent) {
        when (event) {
            is ProfileEditEvent.Load -> loadProfile(event.userId)
            is ProfileEditEvent.NameChanged -> {
                _uiState.update { it.copy(editedName = event.name, isSaved = false) }
            }
            is ProfileEditEvent.SurnameChanged -> {
                _uiState.update { it.copy(editedSurname = event.surname, isSaved = false) }
            }
            is ProfileEditEvent.BioChanged -> {
                _uiState.update { it.copy(editedBio = event.bio, isSaved = false) }
            }
            is ProfileEditEvent.CityChanged -> {
                _uiState.update { it.copy(editedCity = event.city, isSaved = false) }
            }
            is ProfileEditEvent.InterestToggled -> toggleInterest(event.interestId)
            is ProfileEditEvent.Save -> saveProfile()
            is ProfileEditEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun loadProfile(userId: String?) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val user = if (userId != null) {
                getUserByIdUseCase(userId).getOrNull()
            } else {
                getCurrentUserUseCase().getOrNull()
            }

            if (user != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        editedName = user.name,
                        editedSurname = user.surname,
                        editedBio = user.bio,
                        editedCity = user.city,
                        selectedInterestIds = user.interests.map { tag -> tag.id },
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, error = ErrorType.Unknown)
                }
            }
        }
    }

    private fun toggleInterest(interestId: Long) {
        _uiState.update { state ->
            val current = state.selectedInterestIds.toMutableList()
            if (current.contains(interestId)) {
                current.remove(interestId)
            } else {
                current.add(interestId)
            }
            state.copy(selectedInterestIds = current, isSaved = false)
        }
    }

    private fun saveProfile() {
        val state = _uiState.value
        val currentUser = state.user ?: return

        scope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val updatedUser = currentUser.copy(
                name = state.editedName,
                surname = state.editedSurname,
                bio = state.editedBio,
                city = state.editedCity,
            )

            updateUserProfileUseCase(
                user = updatedUser,
                interestIds = state.selectedInterestIds,
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(isSaving = false, isSaved = true)
                    }
                    _navEvent.emit(ProfileNavEvent.NavigateBack)
                }
                .onFailure { throwable ->
                    val errorType = (throwable as? ApiException)
                        ?.toErrorType() ?: ErrorType.Unknown
                    _uiState.update {
                        it.copy(isSaving = false, error = errorType)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
