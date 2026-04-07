package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCodeResponse(
    @SerialName("retryAfterSeconds") val retryAfterSeconds: Int
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserProfile,
    @SerialName("isNewUser") val isNewUser: Boolean,
    @SerialName("isRecovered") val isRecovered: Boolean
)