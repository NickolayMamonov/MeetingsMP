package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.AuthRepository

/**
 * Use case for logging out the current user.
 *
 * Invalidates the server session and clears the locally stored auth token.
 */
class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    /**
     * Logs out the current user.
     *
     * @return [Result] with Unit on success, or a failure.
     */
    suspend operator fun invoke(): Result<Unit> =
        authRepository.logout()
}
