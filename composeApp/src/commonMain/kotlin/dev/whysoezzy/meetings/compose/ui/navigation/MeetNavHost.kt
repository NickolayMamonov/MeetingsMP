package dev.whysoezzy.meetings.compose.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.ui.auth.AuthSuccessScreen
import dev.whysoezzy.meetings.compose.ui.auth.CodeVerificationScreen
import dev.whysoezzy.meetings.compose.ui.auth.NameInputScreen
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputScreen
import dev.whysoezzy.meetings.compose.ui.communities.CommunityDetailsScreen
import dev.whysoezzy.meetings.compose.ui.communities.CommunitySubscribersScreen
import dev.whysoezzy.meetings.compose.ui.meetings.MainScreen
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingDetailsScreen
import dev.whysoezzy.meetings.compose.ui.meetings.MeetingParticipantsScreen
import dev.whysoezzy.meetings.compose.ui.profile.ProfileDetailsScreen
import dev.whysoezzy.meetings.compose.ui.profile.ProfileEditScreen
import dev.whysoezzy.meetings.compose.ui.splash.SplashScreen

/**
 * Main navigation host that renders screens based on the current [MeetRoute].
 *
 * Auth gate: if the user is logged in, shows the main graph;
 * otherwise shows the auth flow starting from splash/phone input.
 *
 * @param navController The navigation controller driving routing.
 */
@Suppress("LongMethod")
@Composable
fun MeetNavHost(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
) {
    val currentRoute by rememberUpdatedState(navController.currentRoute)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background),
    ) {
        AnimatedContent(
            targetState = currentRoute,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "MeetNavHost",
        ) { route ->
            when (route) {
                // ── Splash ────────────────────────────────────────
                is MeetRoute.Splash -> {
                    SplashScreen(navController = navController)
                }

                // ── Auth graph ────────────────────────────────────
                is MeetRoute.PhoneInput -> {
                    PhoneInputScreen(navController = navController)
                }
                is MeetRoute.CodeVerification -> {
                    CodeVerificationScreen(
                        phone = route.phone,
                        navController = navController,
                    )
                }
                is MeetRoute.NameInput -> {
                    NameInputScreen(navController = navController)
                }
                is MeetRoute.AuthSuccess -> {
                    AuthSuccessScreen(navController = navController)
                }

                // ── Meetings graph ────────────────────────────────
                is MeetRoute.MainScreen -> {
                    MainScreen(navController = navController)
                }
                is MeetRoute.MeetingDetails -> {
                    MeetingDetailsScreen(
                        meetingId = route.meetingId,
                        navController = navController,
                    )
                }
                is MeetRoute.MeetingParticipants -> {
                    MeetingParticipantsScreen(
                        meetingId = route.meetingId,
                        navController = navController,
                    )
                }

                // ── Communities graph ─────────────────────────────
                is MeetRoute.CommunityDetails -> {
                    CommunityDetailsScreen(
                        communityId = route.communityId,
                        navController = navController,
                    )
                }
                is MeetRoute.CommunitySubscribers -> {
                    CommunitySubscribersScreen(
                        communityId = route.communityId,
                        navController = navController,
                    )
                }

                // ── Profile graph ─────────────────────────────────
                is MeetRoute.ProfileDetails -> {
                    ProfileDetailsScreen(navController = navController)
                }
                is MeetRoute.ProfileEdit -> {
                    ProfileEditScreen(navController = navController)
                }
            }
        }
    }
}
