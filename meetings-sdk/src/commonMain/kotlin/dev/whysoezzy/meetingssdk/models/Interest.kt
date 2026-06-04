package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.Serializable

/**
 * Интерес / тег для категоризации контента.
 *
 * Используется для фильтрации событий, сообществ и рекомендаций.
 *
 * @property id Идентификатор интереса.
 * @property name Название интереса.
 */
@Serializable
data class Interest(
    val id: String,
    val name: String
)