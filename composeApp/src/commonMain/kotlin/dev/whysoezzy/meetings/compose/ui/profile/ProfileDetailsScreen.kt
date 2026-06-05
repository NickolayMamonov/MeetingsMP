package dev.whysoezzy.meetings.compose.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import dev.whysoezzy.meetings.compose.components.UIKitButton
import dev.whysoezzy.meetings.compose.components.UIKitButtonOutlined
import dev.whysoezzy.meetings.compose.components.UIKitSocialMediaList
import dev.whysoezzy.meetings.compose.components.UIKitUserCommunitiesBlock
import dev.whysoezzy.meetings.compose.components.UIKitUserMeetingsBlock
import dev.whysoezzy.meetings.compose.components.UIKitUserProfileBlock
import dev.whysoezzy.meetings.compose.mapper.toUIKit
import dev.whysoezzy.meetings.compose.mapper.toUIKitInfo
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.viewmodel.ProfileDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Profile details screen for the current user.
 */
@Suppress("UnusedParameter")
@Composable
fun ProfileDetailsScreen(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is ProfileNavEvent.NavigateToEditProfile -> {
                    navController.navigate(MeetRoute.ProfileEdit)
                }
                is ProfileNavEvent.NavigateToLogin -> {
                    navController.navigateAndClear(MeetRoute.PhoneInput)
                }
                is ProfileNavEvent.NavigateBack -> {
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
        uiState.user != null -> {
            ProfileDetailsContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onEditClick = { navController.navigate(MeetRoute.ProfileEdit) },
            )
        }
    }
}

@Composable
private fun ProfileDetailsContent(
    uiState: ProfileDetailsUiState,
    onEvent: (ProfileDetailsEvent) -> Unit,
    onEditClick: () -> Unit,
) {
    val user = uiState.user ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        UIKitUserProfileBlock(
            avatarUrl = user.avatar,
            name = user.name,
            surname = user.surname,
            bio = user.bio,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Social media
        if (user.socialMedias.isNotEmpty()) {
            UIKitSocialMediaList(
                socialMedias = user.socialMedias.map { it.toUIKit() },
                modifier = Modifier.padding(horizontal = SpacingTokens.medium),
            )
            Spacer(modifier = Modifier.height(SpacingTokens.medium))
        }

        // User's meetings
        if (user.showMeetings && uiState.userMeetings.isNotEmpty()) {
            UIKitUserMeetingsBlock(
                title = "Мои встречи",
                meetings = uiState.userMeetings.map { it.toUIKitInfo() },
                onMeetingClick = {},
            )
            Spacer(modifier = Modifier.height(SpacingTokens.medium))
        }

        // User's communities
        if (user.showCommunities && uiState.userCommunities.isNotEmpty()) {
            UIKitUserCommunitiesBlock(
                title = "Мои сообщества",
                communities = uiState.userCommunities.map { it.toUIKitInfo() },
                onCommunityClick = {},
            )
            Spacer(modifier = Modifier.height(SpacingTokens.medium))
        }

        // Edit profile button
        UIKitButton(
            text = "Редактировать профиль",
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.medium),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        // Logout button
        UIKitButtonOutlined(
            text = "Выйти",
            onClick = { onEvent(ProfileDetailsEvent.Logout) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.medium),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))
    }
}
