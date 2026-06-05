package dev.whysoezzy.meetings.compose.ui.profile

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.User

data class ProfileEditUiState(
    val user: User? = null,
    val editedName: String = "",
    val editedSurname: String = "",
    val editedBio: String = "",
    val editedCity: String = "",
    val allTags: List<Tag> = emptyList(),
    val selectedInterestIds: List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: ErrorType? = null,
    val isSaved: Boolean = false,
)
