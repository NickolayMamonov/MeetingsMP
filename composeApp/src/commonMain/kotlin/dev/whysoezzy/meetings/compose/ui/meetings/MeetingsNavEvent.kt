package dev.whysoezzy.meetings.compose.ui.meetings

/**
 * Navigation events emitted by meetings ViewModels.
 */
sealed interface MeetingNavEvent {
    data object NavigateBack : MeetingNavEvent
}
