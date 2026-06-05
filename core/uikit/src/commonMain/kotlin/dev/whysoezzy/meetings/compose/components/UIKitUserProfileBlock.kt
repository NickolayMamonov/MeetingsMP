package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A profile header block displaying user avatar, name, and bio.
 *
 * @param avatarUrl URL of the user's avatar image.
 * @param name The user's first name.
 * @param surname The user's surname.
 * @param bio Optional biography text.
 * @param modifier Modifier applied to the block.
 */
@Composable
fun UIKitUserProfileBlock(
    avatarUrl: String,
    name: String,
    surname: String,
    modifier: Modifier = Modifier,
    bio: String = "",
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UIKitAvatarLarge(
            imageUrl = avatarUrl,
            initials = "${name.first()}${surname.first()}",
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        Text(
            text = "$name $surname",
            style = MeetingsTheme.typography.headlineMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        if (bio.isNotEmpty()) {
            Spacer(modifier = Modifier.height(SpacingTokens.small))
            Text(
                text = bio,
                style = MeetingsTheme.typography.bodyMedium,
                color = MeetingsTheme.colors.onSurfaceVariant,
            )
        }
    }
}
