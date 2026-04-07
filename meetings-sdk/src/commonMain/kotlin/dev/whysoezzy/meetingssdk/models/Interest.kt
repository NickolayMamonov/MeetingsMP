package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.Serializable

@Serializable
data class Interest(
    val id: String,
    val name: String
)