package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A block displaying a user's communities list with a section header.
 *
 * @param title Section title (e.g., "My Communities").
 * @param communities List of communities to display.
 * @param onCommunityClick Callback invoked when a community is clicked.
 * @param modifier Modifier applied to the block.
 */
@Composable
fun UIKitUserCommunitiesBlock(
    title: String,
    communities: List<UIKitCommunityInfo>,
    onCommunityClick: (UIKitCommunityInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MeetingsTheme.typography.titleLarge,
            color = MeetingsTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = SpacingTokens.medium),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.small),
            contentPadding = PaddingValues(SpacingTokens.medium),
        ) {
            items(communities, key = { it.id }) { community ->
                UIKitCommunityCard(
                    community = community,
                    onClick = { onCommunityClick(community) },
                )
            }
        }
    }
}
