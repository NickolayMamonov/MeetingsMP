package dev.whysoezzy.meetings.compose.ui.meetings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.components.UIKitCommunityCard
import dev.whysoezzy.meetings.compose.components.UIKitEventCard
import dev.whysoezzy.meetings.compose.components.UIKitTag
import dev.whysoezzy.meetings.compose.models.UIKitTagState
import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.models.UIKitMeeting
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.models.UIKitTag as UIKitTagModel
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.viewmodel.MainScreenUiState
import dev.whysoezzy.meetings.compose.viewmodel.MainScreenEvent
import dev.whysoezzy.meetings.compose.viewmodel.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Main screen with feed, hero meeting, nearest meetings, recommended communities,
 * tag filters, and meetings list.
 */
@Composable
fun MainScreen(
    navController: MeetNavController,
    viewModel: MainScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is dev.whysoezzy.meetings.compose.viewmodel.MainScreenNavEvent.MeetingDetails -> {
                    navController.navigate(MeetRoute.MeetingDetails(meetingId = event.meetingId.toString()))
                }
                is dev.whysoezzy.meetings.compose.viewmodel.MainScreenNavEvent.MeetingParticipants -> {
                    navController.navigate(MeetRoute.MeetingParticipants(meetingId = event.meetingId.toString()))
                }
                is dev.whysoezzy.meetings.compose.viewmodel.MainScreenNavEvent.CommunityDetails -> {
                    navController.navigate(MeetRoute.CommunityDetails(communityId = event.communityId))
                }
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MeetingsTheme.colors.primary)
            }
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState.error ?: "Error",
                    style = MeetingsTheme.typography.bodyLarge,
                    color = MeetingsTheme.colors.error,
                )
            }
        }
        else -> {
            MainScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onMeetingClick = { meetingId ->
                    navController.navigate(MeetRoute.MeetingDetails(meetingId = meetingId.toString()))
                },
                onCommunityClick = { communityId ->
                    navController.navigate(MeetRoute.CommunityDetails(communityId = communityId))
                },
            )
        }
    }
}

@Composable
private fun MainScreenContent(
    uiState: MainScreenUiState,
    onEvent: (MainScreenEvent) -> Unit,
    onMeetingClick: (Long) -> Unit,
    onCommunityClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background),
        contentPadding = PaddingValues(SpacingTokens.medium),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.medium),
    ) {
        // Hero meeting
        uiState.heroMeeting?.let { hero ->
            item(key = "hero") {
                UIKitEventCard(
                    meeting = UIKitMeetingInfo(
                        id = hero.id,
                        imageUrl = hero.imageUrl,
                        title = hero.title,
                        address = hero.address.address,
                        tags = hero.tags,
                        time = hero.time,
                        meetingStatus = hero.meetingStatus,
                    ),
                    onClick = { onMeetingClick(hero.id) },
                )
            }
        }

        // Nearest meetings
        if (uiState.nearestMeetings.isNotEmpty()) {
            item(key = "nearest_header") {
                Text(
                    text = "Ближайшие встречи",
                    style = MeetingsTheme.typography.titleLarge,
                    color = MeetingsTheme.colors.onSurface,
                )
            }

            item(key = "nearest_list") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
                ) {
                    items(uiState.nearestMeetings, key = { it.id }) { meeting ->
                        UIKitEventCard(
                            meeting = UIKitMeetingInfo(
                                id = meeting.id,
                                imageUrl = meeting.imageUrl,
                                title = meeting.title,
                                address = meeting.address.address,
                                tags = meeting.tags,
                                time = meeting.time,
                                meetingStatus = meeting.meetingStatus,
                            ),
                            onClick = { onMeetingClick(meeting.id) },
                            modifier = Modifier.width(280.dp),
                        )
                    }
                }
            }
        }

        // Recommended communities
        if (uiState.recommendedCommunities.isNotEmpty()) {
            item(key = "communities_header") {
                Text(
                    text = "Рекомендуемые сообщества",
                    style = MeetingsTheme.typography.titleLarge,
                    color = MeetingsTheme.colors.onSurface,
                )
            }

            items(uiState.recommendedCommunities, key = { "community_${it.id}" }) { community ->
                UIKitCommunityCard(
                    community = UIKitCommunityInfo(
                        id = community.id,
                        name = community.name,
                        description = community.description,
                        imageUrl = community.imageUrl,
                        subscribersCount = community.subscribersCount,
                        isSubscribed = community.isSubscribed,
                    ),
                    onClick = { onCommunityClick(community.id) },
                )
            }
        }

        // Tag filters
        if (uiState.tags.isNotEmpty()) {
            item(key = "tag_filters") {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
                ) {
                    uiState.tags.forEach { tag ->
                        val isSelected = uiState.selectedTagId == tag.id
                        UIKitTag(
                            text = tag.name,
                            state = if (isSelected) UIKitTagState.SELECTED else UIKitTagState.ACTIVE,
                            onClick = { onEvent(MainScreenEvent.TagSelected(tag.id)) },
                        )
                    }
                }
            }
        }

        // All meetings
        if (uiState.allMeetings.isNotEmpty()) {
            item(key = "all_meetings_header") {
                Text(
                    text = "Все встречи",
                    style = MeetingsTheme.typography.titleLarge,
                    color = MeetingsTheme.colors.onSurface,
                )
            }

            items(uiState.allMeetings, key = { "meeting_${it.id}" }) { meeting ->
                UIKitEventCard(
                    meeting = UIKitMeetingInfo(
                        id = meeting.id,
                        imageUrl = meeting.imageUrl,
                        title = meeting.title,
                        address = meeting.address.address,
                        tags = meeting.tags,
                        time = meeting.time,
                        meetingStatus = meeting.meetingStatus,
                    ),
                    onClick = { onMeetingClick(meeting.id) },
                )
            }
        }
    }
}
