package dev.whysoezzy.meetingssdk

import de.jensklingenberg.ktorfit.Ktorfit
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
import dev.whysoezzy.meetingssdk.models.RequestCodeBody
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse
import dev.whysoezzy.meetingssdk.models.VerifyCodeBody
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Main client for the Meetings API. Provides access to all API endpoints
 * and manages authentication state.
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
    private val tokenProvider: TokenProvider
){
    private val ktorfit by lazy {
        Ktorfit.Builder()
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
     * Request a verification code to be sent to the given phone number.
     *
     * @param phone Phone number to send the code to.
     * @param firstName User's first name for registration.
     * @return [RequestCodeResponse] with retry timing information.
     */
    suspend fun requestCode(phone: String, firstName: String): RequestCodeResponse {
        return auth.requestCode(RequestCodeBody(phone, firstName))
    }

    /**
     * Verify the code sent to the user's phone.
     * On success, stores the authentication token via the [tokenProvider].
     *
     * @param phone Phone number that received the code.
     * @param code The verification code to validate.
     * @return [AuthResponse] with the authentication token and user profile.
     */
    suspend fun verifyCode(phone: String, code: String): AuthResponse {
        val response = auth.verifyCode(VerifyCodeBody(phone, code))
        tokenProvider.setToken(AuthToken(response.token))
        return response
    }

    /**
     * Log out the current user by invalidating the session on the server
     * and clearing the local authentication token.
     */
    suspend fun logout() {
        auth.logout()
        tokenProvider.clear()
    }

    /** Whether the client has a stored authentication token. */
    val isAuthenticated: Boolean
        get() = tokenProvider.getToken() != null
}

/**
 * Factory function for creating a [MeetingsClient] with a pre-configured HTTP client.
 *
 * @param baseUrl Base URL for the API server. Defaults to a local development server.
 * @param tokenProvider Strategy for token storage. Defaults to [InMemoryTokenProvider].
 * @param enableLogging Whether to enable HTTP request/response logging.
 * @param json Custom JSON configuration for serialization. Uses sensible defaults if not provided.
 * @return A configured [MeetingsClient] instance.
 */
fun MeetingsClient(
    baseUrl: String = "http://localhost:8080/",
    tokenProvider: TokenProvider = InMemoryTokenProvider(),
    enableLogging: Boolean = false,
    json: Json = defaultJson
): MeetingsClient {
    val httpClient = defaultHttpClient(json, tokenProvider, enableLogging)
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
    enableLogging: Boolean
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getToken()?.let { authToken ->
                        BearerTokens(accessToken = authToken.token, refreshToken = null)
                    }
                }
                sendWithoutRequest { true }
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

    /** API для аутентификации пользователей. */
    @Suppress("DEPRECATION")
    val auth: AuthApi by lazy { ktorfit.create<AuthApi>() }

    /** API для получения ленты событий и рекомендаций. */
    @Suppress("DEPRECATION")
    val feed: FeedApi by lazy { ktorfit.create<FeedApi>() }

    /** API для работы с событиями. */
    @Suppress("DEPRECATION")
    val events: EventsApi by lazy { ktorfit.create<EventsApi>() }

    /** API для работы с сообществами. */
    @Suppress("DEPRECATION")
    val communities: CommunitiesApi by lazy { ktorfit.create<CommunitiesApi>() }

    /** API для управления пользователями. */
    @Suppress("DEPRECATION")
    val users: UsersApi by lazy { ktorfit.create<UsersApi>() }

    /** API для работы с интересами. */
    @Suppress("DEPRECATION")
    val interests: InterestsApi by lazy { ktorfit.create<InterestsApi>() }

    /**
     * Отправляет код подтверждения на указанный номер телефона.
     *
     * @param phone Номер телефона.
     * @param firstName Имя пользователя.
     * @return Ответ с указанием времени до следующей отправки кода.
     */
    suspend fun requestCode(phone: String, firstName: String): RequestCodeResponse {
        return auth.requestCode(RequestCodeBody(phone, firstName))
    }

    /**
     * Подтверждает код аутентификации и сохраняет JWT-токен.
     *
     * При успешной верификации токен автоматически сохраняется
     * через [TokenProvider] и будет передаваться в последующих запросах.
     *
     * @param phone Номер телефона.
     * @param code Код подтверждения, полученный по SMS.
     * @return Ответ с JWT-токеном и профилем пользователя.
     */
    suspend fun verifyCode(phone: String, code: String): AuthResponse {
        val response = auth.verifyCode(VerifyCodeBody(phone, code))
        tokenProvider.setToken(AuthToken(response.token))
        return response
    }

    /**
     * Выполняет выход из системы.
     *
     * Инвалидирует токен на сервере и очищает локальное хранилище токена.
     */
    suspend fun logout() {
        auth.logout()
        tokenProvider.clear()
    }

    /**
     * Флаг, указывающий, аутентифицирован ли текущий пользователь.
     *
     * `true`, если в [TokenProvider] сохранён действующий JWT-токен.
     */
    val isAuthenticated: Boolean
        get() = tokenProvider.getToken() != null
}

/**
 * Создаёт экземпляр [MeetingsClient] с настраиваемыми параметрами.
 *
 * @param baseUrl Базовый URL API-сервера. По умолчанию `http://localhost:8080/`.
 * @param tokenProvider Провайдер JWT-токена. По умолчанию [InMemoryTokenProvider].
 * @param enableLogging Включить логирование HTTP-запросов и ответов.
 * @param json Конфигурация сериализации JSON. По умолчанию используется
 *   [defaultJson] с игнорированием неизвестных полей.
 * @return Новый экземпляр [MeetingsClient].
 */
fun MeetingsClient(
    baseUrl: String = "http://localhost:8080/",
    tokenProvider: TokenProvider = InMemoryTokenProvider(),
    enableLogging: Boolean = false,
    json: Json = defaultJson
): MeetingsClient {
    val httpClient = defaultHttpClient(json, tokenProvider, enableLogging)
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
    enableLogging: Boolean
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getToken()?.let { authToken ->
                        BearerTokens(accessToken = authToken.token, refreshToken = null)
                    }
                }
                sendWithoutRequest { true }
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