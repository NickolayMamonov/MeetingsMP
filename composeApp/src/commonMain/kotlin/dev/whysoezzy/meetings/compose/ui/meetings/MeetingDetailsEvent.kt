package dev.whysoezzy.meetings.compose.ui.meetings

sealed interface MeetingDetailsEvent {
    data class Load(val meetingId: String) : MeetingDetailsEvent
    data object Join : MeetingDetailsEvent
    data object Leave : MeetingDetailsEvent
    data object ClearError : MeetingDetailsEvent
}
