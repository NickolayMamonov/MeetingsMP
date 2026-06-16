package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response after requesting a verification code.
 *
 * @deprecated The /auth/request-code endpoint has been replaced by /auth/send-otp.
 *   Use the new [AuthResponse] flow instead. This class is kept for backward
 *   compatibility with the old backend contract and will be removed in a future version.
 *
 * @property retryAfterSeconds Number of seconds the client should wait before requesting a new code.
 */
@Serializable
@Deprecated("Use AuthResponse via /auth/send-otp + /auth/verify-otp instead")
data class RequestCodeResponse(
    @SerialName("retryAfterSeconds") val retryAfterSeconds: Int
)

/**
 * Response after successfully verifying the authentication code.
 *
 * Contains both access and refresh tokens, the user profile, and registration status.
 * The access token is a JWT (7-day lifetime) used for API authentication.
 * The refresh token is a UUID (30-day lifetime) used to obtain new access tokens.
 *
 * @property accessToken JWT access token for authenticating subsequent API requests.
 * @property refreshToken UUID refresh token for obtaining new access tokens.
 * @property user The authenticated user's full profile.
 * @property isNewUser Whether this is a newly registered user.
 * @property isRecovered Whether the account was recovered from a previous registration.
 */
@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserProfile,
    @SerialName("isNewUser") val isNewUser: Boolean = false,
    @SerialName("isRecovered") val isRecovered: Boolean = false,
)

/**
 * Response after successfully refreshing an access token.
 *
 * Contains only the new access token — the refresh token is not rotated
 * and remains valid until its expiration (30 days) or explicit logout.
 *
 * @property accessToken New JWT access token.
 */
@Serializable
data class RefreshTokenResponse(
    val accessToken: String,
)
