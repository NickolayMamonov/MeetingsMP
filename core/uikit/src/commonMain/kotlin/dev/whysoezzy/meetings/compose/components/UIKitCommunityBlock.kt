package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A detailed community information block with avatar, name, description, and subscribe button.
 *
 * @param community The community to display.
 * @param onSubscribeClick Callback invoked when the subscribe button is clicked.
 * @param modifier Modifier applied to the block.
 */
@Composable
fun UIKitCommunityBlock(
    community: UIKitCommunity,
    onSubscribeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UIKitAvatarLarge(
            imageUrl = community.imageUrl,
            initials = community.name.take(2),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        Text(
            text = community.name,
            style = MeetingsTheme.typography.headlineMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        if (community.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(SpacingTokens.small))
            Text(
                text = community.description,
                style = MeetingsTheme.typography.bodyMedium,
                color = MeetingsTheme.colors.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        Text(
            text = "${community.subscribersCount} subscribers",
            style = MeetingsTheme.typography.labelMedium,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        UIKitSubscribeButton(
            isSubscribed = community.isSubscribed,
            onClick = onSubscribeClick,
        )
    }
}
