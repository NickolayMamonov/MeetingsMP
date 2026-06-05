package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.AuthRepository

/**
 * Use case for checking whether a user is currently authenticated.
 *
 * Returns `true` if a valid auth token is stored locally,
 * `false` otherwise. This does not make a network call.
 */
class IsLoggedInUseCase(
    private val authRepository: AuthRepository,
) {
    /**
     * Checks if the user has a stored authentication token.
     *
     * @return `true` if authenticated, `false` otherwise.
     */
    operator fun invoke(): Boolean =
        authRepository.isLoggedIn()
}
