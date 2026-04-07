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

    @Suppress("DEPRECATION")
    val auth: AuthApi by lazy { ktorfit.create<AuthApi>() }

    @Suppress("DEPRECATION")
    val feed: FeedApi by lazy { ktorfit.create<FeedApi>() }

    @Suppress("DEPRECATION")
    val events: EventsApi by lazy { ktorfit.create<EventsApi>() }

    @Suppress("DEPRECATION")
    val communities: CommunitiesApi by lazy { ktorfit.create<CommunitiesApi>() }

    @Suppress("DEPRECATION")
    val users: UsersApi by lazy { ktorfit.create<UsersApi>() }

    @Suppress("DEPRECATION")
    val interests: InterestsApi by lazy { ktorfit.create<InterestsApi>() }

    suspend fun requestCode(phone: String,firstName: String): RequestCodeResponse{
        return auth.requestCode(RequestCodeBody(phone,firstName))
    }

    suspend fun verifyCode(phone: String, code: String): AuthResponse{
        val response = auth.verifyCode(VerifyCodeBody(phone,code))
        tokenProvider.setToken(AuthToken(response.token))
        return response
    }

    suspend fun logout(){
        auth.logout()
        tokenProvider.clear()
    }

    val isAuthenticated: Boolean
        get() = tokenProvider.getToken() != null
}

fun MeetingsClient(
    baseUrl: String = "http://localhost:8080/",
    tokenProvider: TokenProvider = InMemoryTokenProvider(),
    enableLogging: Boolean = false,
    json: Json = defaultJson
): MeetingsClient{
    val httpClient = defaultHttpClient(json,tokenProvider,enableLogging)
    return MeetingsClient(baseUrl,httpClient,tokenProvider)
}

private val defaultJson = Json{
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

        install(Auth){
            bearer {
                loadTokens {
                    tokenProvider.getToken()?.let { authToken ->
                        BearerTokens(accessToken = authToken.token, refreshToken = null)
                    }
                }
                sendWithoutRequest { true }
            }
        }

        if(enableLogging){
            install(Logging){
                level = LogLevel.BODY
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }


}