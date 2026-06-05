package dev.whysoezzy.meetings.compose.ui.meetings

sealed interface MeetingsNavEvent {
    data class NavigateToMeetingDetails(val meetingId: String) : MeetingsNavEvent
    data object NavigateToCreateMeeting : MeetingsNavEvent
    data object NavigateBack : MeetingsNavEvent
    data class NavigateToMeetingParticipants(val meetingId: String) : MeetingsNavEvent
}
