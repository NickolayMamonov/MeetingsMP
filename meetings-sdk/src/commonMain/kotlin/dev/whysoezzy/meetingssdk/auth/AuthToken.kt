package dev.whysoezzy.meetingssdk.auth

import kotlin.jvm.JvmInline

/**
 * An inline value class wrapping a JWT bearer token for API authentication.
 *
 * @property token The raw JWT token string.
 */
@JvmInline
value class AuthToken(val token: String)