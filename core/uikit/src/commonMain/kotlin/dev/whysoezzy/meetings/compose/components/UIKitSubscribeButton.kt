@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A subscribe/unsubscribe toggle button for communities.
 *
 * @param isSubscribed Whether the user is currently subscribed.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier applied to the button.
 * @param subscribedText Text shown when subscribed.
 * @param unsubscribedText Text shown when not subscribed.
 * @param icon Optional icon to display.
 */
@Composable
fun UIKitSubscribeButton(
    isSubscribed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subscribedText: String = "Subscribed",
    unsubscribedText: String = "Subscribe",
    icon: ImageVector? = null,
) {
    val colors = MeetingsTheme.colors
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSubscribed) colors.surfaceVariant else colors.primary,
        animationSpec = tween(durationMillis = 200),
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (isSubscribed) colors.onSurfaceVariant else colors.onPrimary,
        animationSpec = tween(durationMillis = 200),
    )

    val shape = MeetingsTheme.shapes.full

    Row(
        modifier = modifier
            .clip(shape)
            .background(animatedBackgroundColor)
            .clickable(onClick = onClick)
            .height(SpacingTokens.chipHeight)
            .padding(horizontal = SpacingTokens.medium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = animatedContentColor,
            )
        }
        Text(
            text = if (isSubscribed) subscribedText else unsubscribedText,
            color = animatedContentColor,
            style = MeetingsTheme.typography.labelSmall,
        )
    }
}
