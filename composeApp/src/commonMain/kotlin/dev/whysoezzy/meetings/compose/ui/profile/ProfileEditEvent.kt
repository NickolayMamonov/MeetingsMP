package dev.whysoezzy.meetings.compose.ui.profile

sealed interface ProfileEditEvent {
    data class Load(val userId: String?) : ProfileEditEvent
    data class NameChanged(val name: String) : ProfileEditEvent
    data class SurnameChanged(val surname: String) : ProfileEditEvent
    data class BioChanged(val bio: String) : ProfileEditEvent
    data class CityChanged(val city: String) : ProfileEditEvent
    data class InterestToggled(val interestId: Long) : ProfileEditEvent
    data object Save : ProfileEditEvent
    data object ClearError : ProfileEditEvent
}
