package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A composable that displays an avatar image or initials placeholder.
 *
 * @param imageUrl URL of the avatar image. If empty, initials are shown.
 * @param initials Up to 2 characters to display when no image is available.
 * @param modifier Modifier applied to the avatar.
 * @param size The size of the avatar. Defaults to [SpacingTokens.avatarSize].
 */
@Composable
fun UIKitAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    initials: String = "",
    size: Dp = SpacingTokens.avatarSize,
) {
    val shape = CircleShape
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(MeetingsTheme.colors.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNotEmpty()) {
            // Coil/Kamel image loading would go here in platform-specific code
            // For now, show initials as placeholder
            AvatarInitials(initials = initials)
        } else {
            AvatarInitials(initials = initials)
        }
    }
}

@Composable
private fun AvatarInitials(initials: String) {
    if (initials.isNotEmpty()) {
        Text(
            text = initials.take(2).uppercase(),
            color = MeetingsTheme.colors.onSurface,
            style = MeetingsTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * A small avatar variant with border for overlapping avatar lists.
 *
 * @param imageUrl URL of the avatar image.
 * @param initials Up to 2 characters to display when no image is available.
 * @param modifier Modifier applied to the avatar.
 */
@Composable
fun UIKitAvatarSmall(
    imageUrl: String,
    modifier: Modifier = Modifier,
    initials: String = "",
) {
    UIKitAvatar(
        imageUrl = imageUrl,
        initials = initials,
        modifier = modifier.border(
            width = 2.dp,
            color = MeetingsTheme.colors.surface,
            shape = CircleShape
        ),
        size = SpacingTokens.avatarSizeSmall,
    )
}

/**
 * A large avatar variant for profile screens.
 *
 * @param imageUrl URL of the avatar image.
 * @param initials Up to 2 characters to display when no image is available.
 * @param modifier Modifier applied to the avatar.
 */
@Composable
fun UIKitAvatarLarge(
    imageUrl: String,
    modifier: Modifier = Modifier,
    initials: String = "",
) {
    UIKitAvatar(
        imageUrl = imageUrl,
        initials = initials,
        modifier = modifier,
        size = SpacingTokens.avatarSizeLarge,
    )
}
