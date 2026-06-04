package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.Serializable

/**
 * Результат поиска по событиям и сообществам.
 *
 * @property events Список найденных событий.
 * @property communities Список найденных сообществ.
 */
@Serializable
data class SearchResponse(
    val events: List<EventShort>,
    val communities: List<CommunityShort>
)

/**
 * Обобщённый ответ сервера с пагинацией.
 *
 * @param T Тип элементов в списке.
 * @property items Список элементов текущей страницы.
 * @property cursor Курсор для получения следующей страницы. `null`, если страниц больше нет.
 * @property total Общее количество элементов.
 */
@Serializable
data class PaginatedResponse<T>(
    val items: List<T>,
    val cursor: String? = null,
    val total: Int
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
