package dev.whysoezzy.meetings.compose.ui.meetings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dev.whysoezzy.meetings.compose.components.UIKitAddressMapBlock
import dev.whysoezzy.meetings.compose.components.UIKitButton
import dev.whysoezzy.meetings.compose.components.UIKitHostCard
import dev.whysoezzy.meetings.compose.components.UIKitParticipantsBlock
import dev.whysoezzy.meetings.compose.components.UIKitTag
import dev.whysoezzy.meetings.compose.models.UIKitTagState
import dev.whysoezzy.meetings.compose.models.UIKitAddress
import dev.whysoezzy.meetings.compose.models.UIKitHost
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsUiState
import dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsEvent
import dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Meeting details screen showing full meeting information.
 */
@Suppress("UnusedParameter")
@Composable
fun MeetingDetailsScreen(
    meetingId: String,
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: MeetingDetailsViewModel = koinViewModel { parametersOf(meetingId) },
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsNavEvent.Participants -> {
                    navController.navigate(MeetRoute.MeetingParticipants(meetingId = event.meetingId.toString()))
                }
                is dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsNavEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(modifier = modifier, 
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
        uiState.meeting != null -> {
            MeetingDetailsContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onParticipantsClick = {
                    navController.navigate(MeetRoute.MeetingParticipants(meetingId = meetingId))
                },
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

@Suppress("LongMethod", "UnusedParameter")
@Composable
private fun MeetingDetailsContent(
    uiState: MeetingDetailsUiState,
    onEvent: (MeetingDetailsEvent) -> Unit,
    onParticipantsClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val meeting = uiState.meeting ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(SpacingTokens.medium),
    ) {
        // Title
        Text(
            text = meeting.title,
            style = MeetingsTheme.typography.headlineMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        // Description
        Text(
            text = meeting.description,
            style = MeetingsTheme.typography.bodyLarge,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Date and time
        Text(
            text = meeting.date,
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Tags
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
        ) {
            meeting.tags.forEach { tag ->
                UIKitTag(
                    text = tag.text,
                    state = UIKitTagState.ACTIVE,
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Address with map
        UIKitAddressMapBlock(
            address = UIKitAddress(
                address = meeting.address.address,
                latitude = meeting.address.latitude,
                longitude = meeting.address.longitude,
            ),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Host
        meeting.personHost?.let { personHost ->
            UIKitHostCard(
                host = UIKitHost.Person(
                    id = personHost.id,
                    title = "${personHost.name} ${personHost.surname}",
                    description = personHost.description,
                    imageUrl = personHost.imageUrl,
                    name = personHost.name,
                    surname = personHost.surname,
                ),
                onClick = {},
            )
        } ?: meeting.communityHost?.let { communityHost ->
            UIKitHostCard(
                host = UIKitHost.Community(
                    id = communityHost.id,
                    title = communityHost.title,
                    description = communityHost.description,
                    imageUrl = communityHost.imageUrl,
                    meetingsInfo = communityHost.meetingsInfo,
                ),
                onClick = {},
            )
        }

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Participants
        UIKitParticipantsBlock(
            participants = meeting.participants,
        )

        Text(
            text = "Посмотреть всех участников",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.primary,
            modifier = Modifier
                .padding(start = SpacingTokens.medium)
                .clickable { onParticipantsClick() },
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        // Join / Leave button
        UIKitButton(
            text = if (meeting.isUserInParticipants) "Отменить участие" else "Присоединиться",
            onClick = {
                onEvent(
                    if (meeting.isUserInParticipants) MeetingDetailsEvent.LeaveMeeting
                    else MeetingDetailsEvent.JoinMeeting
                )
            },
            enabled = !uiState.isJoining,
            loading = uiState.isJoining,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
