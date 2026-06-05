package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import dev.whysoezzy.meetings.compose.models.UIKitHost
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A card displaying a host (person or community) for an event.
 *
 * @param host The host to display.
 * @param onClick Callback invoked when the card is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
fun UIKitHostCard(
    host: UIKitHost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MeetingsTheme.shapes.medium

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MeetingsTheme.colors.surface)
            .clickable(onClick = onClick)
            .padding(SpacingTokens.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val initials = when (host) {
            is UIKitHost.Person -> "${host.name.first()}${host.surname.first()}"
            is UIKitHost.Community -> host.title.take(2)
        }

        UIKitAvatar(
            imageUrl = host.imageUrl,
            initials = initials,
            modifier = Modifier.size(SpacingTokens.avatarSize),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = SpacingTokens.medium),
        ) {
            Text(
                text = host.title,
                style = MeetingsTheme.typography.titleMedium,
                color = MeetingsTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (host.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SpacingTokens.micro))
                Text(
                    text = host.description,
                    style = MeetingsTheme.typography.bodySmall,
                    color = MeetingsTheme.colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
