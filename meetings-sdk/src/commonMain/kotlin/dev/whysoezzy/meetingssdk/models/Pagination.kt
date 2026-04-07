package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.Serializable


@Serializable
data class SearchResponse(
    val events: List<EventShort>,
    val communities: List<CommunityShort>
)

@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val cursor: String? = null,
    val total: Int
)
