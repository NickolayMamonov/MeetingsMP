package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.safeApiCall
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [AuthRepository] that delegates to [MeetingsClient]
 * for network operations and uses [TokenProvider] for token persistence.
 *
 * Authentication state is observed reactively via [TokenProvider.isLoggedIn],
 * which automatically updates when tokens are saved or cleared.
 */
class AuthRepositoryImpl(
    private val meetingsClient: MeetingsClient,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    override suspend fun sendOtp(phone: String): Result<Unit> {
        return safeApiCall {
            meetingsClient.sendOtp(phone)
        }
    }

    override suspend fun verifyOtp(phone: String, code: String, name: String?, surname: String?): Result<AuthResponse> {
        return safeApiCall {
            meetingsClient.verifyOtp(phone, code, name, surname)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return safeApiCall {
            meetingsClient.logout()
        }
    }

    override fun isLoggedIn(): Boolean = tokenProvider.isLoggedIn.value

    override fun isLoggedInFlow(): Flow<Boolean> = tokenProvider.isLoggedIn
}

