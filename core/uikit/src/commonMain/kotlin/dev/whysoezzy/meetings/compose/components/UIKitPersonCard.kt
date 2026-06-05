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
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A card displaying a person in a list or grid.
 *
 * @param person The person to display.
 * @param onClick Callback invoked when the card is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
fun UIKitPersonCard(
    person: UIKitPerson,
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
        UIKitAvatar(
            imageUrl = person.avatarUrl,
            initials = "${person.name.first()}${person.surname.first()}",
            modifier = Modifier.size(SpacingTokens.avatarSize),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = SpacingTokens.medium),
        ) {
            Text(
                text = "${person.name} ${person.surname}",
                style = MeetingsTheme.typography.titleMedium,
                color = MeetingsTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (person.role.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SpacingTokens.micro))
                Text(
                    text = person.role,
                    style = MeetingsTheme.typography.bodySmall,
                    color = MeetingsTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (person.bio.isNotEmpty()) {
                Spacer(modifier = Modifier.height(SpacingTokens.micro))
                Text(
                    text = person.bio,
                    style = MeetingsTheme.typography.bodySmall,
                    color = MeetingsTheme.colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
