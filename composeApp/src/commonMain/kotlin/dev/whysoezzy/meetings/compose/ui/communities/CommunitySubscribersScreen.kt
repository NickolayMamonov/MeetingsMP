package dev.whysoezzy.meetings.compose.ui.communities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.components.UIKitPersonsGrid
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.viewmodel.CommunitySubscribersViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Community subscribers screen showing list of community subscribers.
 */
@Composable
fun CommunitySubscribersScreen(
    communityId: Long,
    navController: MeetNavController,
    viewModel: CommunitySubscribersViewModel = koinViewModel { parametersOf(communityId) },
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background),
    ) {
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
                UIKitPersonsGrid(
                    persons = uiState.subscribers,
                    onPersonClick = {},
                    modifier = Modifier.padding(top = SpacingTokens.medium),
                )
            }
        }
    }
}
