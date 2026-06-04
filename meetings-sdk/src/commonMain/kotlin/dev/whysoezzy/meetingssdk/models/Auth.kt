package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response after requesting a verification code.
 *
 * @property retryAfterSeconds Number of seconds the client should wait before requesting a new code.
 */
@Serializable
data class RequestCodeResponse(
    @SerialName("retryAfterSeconds") val retryAfterSeconds: Int
)

/**
 * Response after successfully verifying the authentication code.
 *
 * @property token Bearer token for authenticating subsequent API requests.
 * @property user The authenticated user's full profile.
 * @property isNewUser Whether this is a newly registered user.
 * @property isRecovered Whether the account was recovered from a previous registration.
 */
@Serializable
data class AuthResponse(
    val token: String,
    val user: UserProfile,
    @SerialName("isNewUser") val isNewUser: Boolean,
    @SerialName("isRecovered") val isRecovered: Boolean
)
