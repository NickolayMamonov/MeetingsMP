package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.Serializable

/**
 * Ответ сервера при ошибке выполнения запроса.
 *
 * @property error Код ошибки.
 * @property message Человекочитаемое описание ошибки.
 * @property details Дополнительные детали ошибки (ключ — поле, значение — описание).
 */
@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val details: Map<String, String>? = null
)