package dev.whysoezzy.meetings.compose.ui.communities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.components.UIKitCommunityBlock
import dev.whysoezzy.meetings.compose.components.UIKitMeetingsList
import dev.whysoezzy.meetings.compose.mapper.toUIKitInfo
import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.viewmodel.CommunityDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Community details screen with info, meetings, and subscribers.
 */
@Suppress("UnusedParameter")
@Composable
fun CommunityDetailsScreen(
    communityId: Long,
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: CommunityDetailsViewModel = koinViewModel { parametersOf(communityId) },
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is CommunitiesNavEvent.NavigateToCommunitySubscribers -> {
                    navController.navigate(MeetRoute.CommunitySubscribers(communityId = event.communityId))
                }
                is CommunitiesNavEvent.NavigateToCommunityDetails -> {
                    navController.navigate(MeetRoute.CommunityDetails(communityId = event.communityId))
                }
                is CommunitiesNavEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(),
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
                    text = uiState.error.toString(),
                    style = MeetingsTheme.typography.bodyLarge,
                    color = MeetingsTheme.colors.error,
                )
            }
        }
        uiState.community != null -> {
            CommunityDetailsContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onSubscribersClick = {
                    navController.navigate(MeetRoute.CommunitySubscribers(communityId = communityId))
                },
            )
        }
    }
}

@Suppress("UnusedParameter")
@Composable
private fun CommunityDetailsContent(
    uiState: CommunityDetailsUiState,
    onEvent: (CommunityDetailsEvent) -> Unit,
    onSubscribersClick: () -> Unit,
) {
    val community = uiState.community ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        UIKitCommunityBlock(
            community = UIKitCommunity(
                id = community.id,
                name = community.name,
                description = community.description,
                imageUrl = community.imageUrl,
                subscribersCount = community.subscribersCount,
                isSubscribed = community.isSubscribed,
                tags = emptyList(),
            ),
            onSubscribeClick = { onEvent(CommunityDetailsEvent.Subscribe) },
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Upcoming meetings
        if (uiState.meetings.isNotEmpty()) {
            Text(
                text = "Встречи сообщества",
                style = MeetingsTheme.typography.titleLarge,
                color = MeetingsTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = SpacingTokens.medium),
            )

            Spacer(modifier = Modifier.height(SpacingTokens.small))

            UIKitMeetingsList(
                meetings = uiState.meetings.map { it.toUIKitInfo() },
                onMeetingClick = {},
                modifier = Modifier.padding(top = SpacingTokens.micro),
            )
        }
    }
}
