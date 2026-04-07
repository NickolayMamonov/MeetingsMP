package dev.whysoezzy.meetingssdk.auth

interface TokenProvider {
    fun getToken(): AuthToken?
    fun setToken(token: AuthToken?)
    fun clear()
}

class InMemoryTokenProvider : TokenProvider {
    private var token: AuthToken? = null

    override fun getToken(): AuthToken? = token
    override fun setToken(token: AuthToken?) { this.token = token }
    override fun clear() { token = null }
}
