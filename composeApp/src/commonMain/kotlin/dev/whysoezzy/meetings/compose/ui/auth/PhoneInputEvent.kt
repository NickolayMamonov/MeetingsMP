package dev.whysoezzy.meetings.compose.ui.auth

sealed interface PhoneInputEvent {
    data class PhoneChanged(val phone: String) : PhoneInputEvent
    data object SendCode : PhoneInputEvent
    data object ClearError : PhoneInputEvent
}
