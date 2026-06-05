package dev.whysoezzy.meetings.compose.ui.meetings

sealed interface MeetingParticipantsEvent {
    data class Load(val meetingId: String) : MeetingParticipantsEvent
    data class LoadMore(val meetingId: String, val cursor: String) : MeetingParticipantsEvent
    data object ClearError : MeetingParticipantsEvent
}
