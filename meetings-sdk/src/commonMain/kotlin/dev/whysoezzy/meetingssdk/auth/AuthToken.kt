package dev.whysoezzy.meetingssdk.auth

/**
 * Authentication token pair containing both access and refresh tokens.
 *
 * @property accessToken JWT access token for API authentication (7-day lifetime).
 * @property refreshToken UUID refresh token for obtaining new access tokens (30-day lifetime).
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)