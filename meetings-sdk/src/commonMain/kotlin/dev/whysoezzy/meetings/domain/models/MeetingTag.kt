package dev.whysoezzy.meetings.domain.models

data class MeetingTag(
    val id: Long,
    val text: String,
    val state: TagState,
)

enum class TagState {
    ACTIVE,
    INACTIVE,
    SELECTED,
    DISABLED,
}
