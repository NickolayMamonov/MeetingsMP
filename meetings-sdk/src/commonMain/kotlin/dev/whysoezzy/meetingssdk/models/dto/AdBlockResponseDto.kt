package dev.whysoezzy.meetingssdk.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdBlockResponseDto(
    val type: String,
    val id: Long,
    val isActive: Boolean,
    val title: String,
    val description: String,
    val communities: List<CommunityInfoDto>? = null,
    val actionText: String? = null,
    val actionUrl: String? = null,
    val users: List<UserInfoDto>? = null,
)
