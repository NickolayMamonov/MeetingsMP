package dev.whysoezzy.meetings.compose.ui.auth

sealed interface NameInputEvent {
    data class FirstNameChanged(val firstName: String) : NameInputEvent
    data class LastNameChanged(val lastName: String) : NameInputEvent
    data object Save : NameInputEvent
    data object ClearError : NameInputEvent
}
