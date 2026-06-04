package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response returned by the search endpoint containing matching events and communities.
 *
 * @property events List of events matching the search query.
 * @property communities List of communities matching the search query.
 */
@Serializable
data class SearchResponse(
    val events: List<EventShort>,
    val communities: List<CommunityShort>
)

/**
 * Generic paginated response with cursor-based pagination.
 *
 * @param T The type of items in the response.
 * @property items List of items for the current page.
 * @property cursor Optional cursor for fetching the next page. `null` if there are no more pages.
 * @property total Total number of items across all pages.
 */
@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val cursor: String? = null,
    val total: Int
)
