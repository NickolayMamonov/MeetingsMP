package dev.whysoezzy.meetings.compose.ui.meetings

sealed interface MainScreenEvent {
    data object Load : MainScreenEvent
    data object Refresh : MainScreenEvent
    data class SelectTag(val tagId: Long) : MainScreenEvent
    data object ClearError : MainScreenEvent
    data class LoadMore(val cursor: String) : MainScreenEvent
}
