package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * Displays a row of overlapping avatar circles, commonly used to show
 * participant counts in a compact format.
 *
 * @param avatarUrls List of avatar image URLs to display.
 * @param modifier Modifier applied to the component.
 * @param maxVisible Maximum number of avatars to show. Extra avatars are hidden.
 * @param overlap The horizontal offset between overlapping avatars.
 */
@Composable
fun UIKitOverlappingAvatars(
    avatarUrls: List<String>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3,
    overlap: Dp = 12.dp,
) {
    val visibleAvatars = avatarUrls.take(maxVisible)

    Box(modifier = modifier) {
        visibleAvatars.forEachIndexed { index, url ->
            UIKitAvatarSmall(
                imageUrl = url,
                initials = "",
                modifier = Modifier.offset(x = (overlap * index)),
            )
        }
    }
}
