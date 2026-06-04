package dev.whysoezzy.meetingssdk.auth

/**
 * Interface for managing authentication tokens.
 * Implementations can store tokens in memory, persistent storage, or secure keychains.
 */
interface TokenProvider {
    /**
     * Retrieves the current authentication token, or `null` if not authenticated.
     */
    fun getToken(): AuthToken?

    /**
     * Stores a new authentication token, or removes it if `null` is passed.
     */
    fun setToken(token: AuthToken?)

    /**
     * Clears the stored authentication token (logs out).
     */
    fun clear()
}

/**
 * Simple in-memory implementation of [TokenProvider].
 * Tokens are stored in a volatile variable and are not persisted across app restarts.
 */
class InMemoryTokenProvider : TokenProvider {
    private var token: AuthToken? = null

    override fun getToken(): AuthToken? = token
    override fun setToken(token: AuthToken?) { this.token = token }
    override fun clear() { token = null }
}
