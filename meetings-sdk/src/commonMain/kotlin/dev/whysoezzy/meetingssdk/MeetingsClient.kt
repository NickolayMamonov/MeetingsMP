package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.api.AuthApi
import dev.whysoezzy.meetingssdk.api.CommunitiesApi
import dev.whysoezzy.meetingssdk.api.EventsApi
import dev.whysoezzy.meetingssdk.api.FeedApi
import dev.whysoezzy.meetingssdk.api.InterestsApi
import dev.whysoezzy.meetingssdk.api.UsersApi
import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.InMemoryTokenProvider
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RefreshTokenBody
import dev.whysoezzy.meetingssdk.models.RefreshTokenResponse
import dev.whysoezzy.meetingssdk.models.SendOtpBody
import dev.whysoezzy.meetingssdk.models.VerifyOtpBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Main client for the Meetings API. Provides access to all API endpoints
 * and manages authentication state with automatic token refresh.
 *
 * Create an instance via the [MeetingsClient] factory function.
 *
 * @property baseUrl Base URL for the API server.
 * @property httpClient Configured Ktor [HttpClient] instance.
 * @property tokenProvider Strategy for storing and retrieving authentication tokens.
 */
class MeetingsClient internal constructor(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val tokenProvider: TokenProvider,
) {
    private val ktorfit by lazy {
        de.jensklingenberg.ktorfit.Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl(baseUrl)
            .build()
    }

    /** Authentication API endpoints. */
    @Suppress("DEPRECATION")
    val auth: AuthApi by lazy { ktorfit.create<AuthApi>() }

    /** Feed API endpoints for main content feed. */
    @Suppress("DEPRECATION")
    val feed: FeedApi by lazy { ktorfit.create<FeedApi>() }

    /** Events API endpoints for event management. */
    @Suppress("DEPRECATION")
    val events: EventsApi by lazy { ktorfit.create<EventsApi>() }

    /** Communities API endpoints for community management. */
    @Suppress("DEPRECATION")
    val communities: CommunitiesApi by lazy { ktorfit.create<CommunitiesApi>() }

    /** Users API endpoints for user profile management. */
    @Suppress("DEPRECATION")
    val users: UsersApi by lazy { ktorfit.create<UsersApi>() }

    /** Interests API endpoints for interest/tag management. */
    @Suppress("DEPRECATION")
    val interests: InterestsApi by lazy { ktorfit.create<InterestsApi>() }

    /**
     * Send a one-time password to the given phone number.
     *
     * @param phone Phone number to send the OTP to.
     * @return Unit on success.
     */
    suspend fun sendOtp(phone: String) {
        auth.sendOtp(SendOtpBody(phone))
    }

    /**
     * Verify the OTP code sent to the user's phone.
     * On success, stores both access and refresh tokens via the [tokenProvider].
     *
     * @param phone Phone number that received the code.
     * @param code The verification code to validate.
     * @param name Optional first name for new user registration.
     * @param surname Optional surname for new user registration.
     * @return [AuthResponse] with access token, refresh token, and user profile.
     */
    suspend fun verifyOtp(phone: String, code: String, name: String? = null, surname: String? = null): AuthResponse {
        val response = auth.verifyOtp(VerifyOtpBody(phone, code, name, surname))
        tokenProvider.saveTokens(AuthToken(response.accessToken, response.refreshToken))
        return response
    }

    /**
     * Log out the current user by invalidating the session on the server
     * and clearing the locally stored tokens.
     */
    suspend fun logout() {
        auth.logout()
        tokenProvider.clearTokens()
    }

    /**
     * Refresh the access token using the stored refresh token.
     * On success, stores the new access token via the [tokenProvider].
     *
     * Note: The refresh token itself is not rotated — the same UUID remains
     * valid until its expiration (30 days) or explicit logout.
     *
     * @return [Result.success] with the new access token string,
     *   or [Result.failure] wrapping an [IllegalStateException] if no
     *   refresh token is available.
     */
    suspend fun refreshAccessToken(): Result<String> {
        val refreshToken = tokenProvider.getRefreshToken()
            ?: return Result.failure(IllegalStateException("No refresh token available for refresh"))
        val response = auth.refreshToken(RefreshTokenBody(refreshToken))
        val currentRefreshToken = tokenProvider.getRefreshToken() ?: refreshToken
        tokenProvider.saveTokens(AuthToken(response.accessToken, currentRefreshToken))
        return Result.success(response.accessToken)
    }

    /** Whether the client has a stored access token. */
    val isAuthenticated: Boolean
        get() = tokenProvider.getAccessToken() != null
}

/**
 * Factory function for creating a [MeetingsClient] with a pre-configured HTTP client.
 *
 * The HTTP client includes:
 * - Bearer token authentication with automatic refresh
 * - Content negotiation (JSON)
 * - Optional request/response logging
 * - Host-scoped token sending (only to the API host)
 * - HTTPS validation for production URLs
 *
 * @param baseUrl Base URL for the API server. Must use HTTPS in production
 *   (HTTP is only allowed for localhost development URLs).
 * @param tokenProvider Strategy for token storage. Defaults to [InMemoryTokenProvider].
 * @param enableLogging Whether to enable HTTP request/response logging.
 * @param allowHttp Whether to allow non-localhost HTTP URLs. Defaults to `false`.
 *   Set to `true` only in debug builds or tests that need plain HTTP.
 * @param json Custom JSON configuration for serialization. Uses sensible defaults if not provided.
 * @return A configured [MeetingsClient] instance.
 * @throws IllegalArgumentException if [baseUrl] uses HTTP and is not localhost
 *   and [allowHttp] is `false`.
 */
fun MeetingsClient(
    baseUrl: String = "http://localhost:8080/",
    tokenProvider: TokenProvider = InMemoryTokenProvider(),
    enableLogging: Boolean = false,
    allowHttp: Boolean = false,
    json: Json = defaultJson,
): MeetingsClient {
    require(allowHttp || baseUrl.startsWith("https://") || "localhost" in baseUrl) {
        "Production baseUrl must use HTTPS: $baseUrl. " +
            "Pass allowHttp = true only in debug builds or tests."
    }
    val expectedApiHost = Url(baseUrl).host
    val httpClient = defaultHttpClient(json, tokenProvider, enableLogging, expectedApiHost, baseUrl)
    return MeetingsClient(baseUrl, httpClient, tokenProvider)
}

private val defaultJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = false
    coerceInputValues = true
}

private fun defaultHttpClient(
    json: Json,
    tokenProvider: TokenProvider,
    enableLogging: Boolean,
    expectedApiHost: String,
    baseUrl: String,
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenProvider.getAccessToken() ?: return@loadTokens null
                    val refreshToken = tokenProvider.getRefreshToken()
                    BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                }
                refreshTokens {
                    val oldRefreshToken = tokenProvider.getRefreshToken()
                        ?: return@refreshTokens null

                    try {
                        val response = authApiRefreshToken(
                            baseUrl = baseUrl,
                            refreshToken = oldRefreshToken,
                            json = json,
                        )
                        val currentRefreshToken = tokenProvider.getRefreshToken() ?: oldRefreshToken
                        tokenProvider.saveTokens(
                            AuthToken(response.accessToken, currentRefreshToken)
                        )
                        BearerTokens(accessToken = response.accessToken, refreshToken = currentRefreshToken)
                    } catch (_: Exception) {
                        // Refresh failed — clear tokens and force re-authentication
                        tokenProvider.clearTokens()
                        null
                    }
                }
                sendWithoutRequest { request ->
                    request.url.host == expectedApiHost
                }
            }
        }

        if (enableLogging) {
            install(Logging) {
                level = LogLevel.BODY
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }
}

/**
 * Performs a manual refresh token request outside the Ktorfit API layer.
 * This is needed because the Bearer plugin's refreshTokens block runs before
 * the Ktorfit instance is fully initialized, so we can't use [AuthApi] directly.
 *
 * @param baseUrl Full base URL of the API server (e.g. "https://api.meetings.mp/").
 * @param refreshToken The refresh token to exchange for a new access token.
 * @param json JSON configuration for request serialization.
 */
private suspend fun authApiRefreshToken(
    baseUrl: String,
    refreshToken: String,
    json: Json,
): RefreshTokenResponse {
    val client = HttpClient {
        install(ContentNegotiation) { json(json) }
    }
    try {
        return client.post("${baseUrl.trimEnd('/')}/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenBody(refreshToken))
        }.body<RefreshTokenResponse>()
    } finally {
        client.close()
    }
}