@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A primary action button following the Meetings design system.
 *
 * @param text The button label text.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier applied to the button.
 * @param enabled Whether the button is enabled for interaction.
 * @param loading Whether the button is in a loading state (shows spinner).
 * @param icon Optional leading icon.
 */
@Composable
fun UIKitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
) {
    val colors = MeetingsTheme.colors
    val (backgroundColor, contentColor) = if (enabled) {
        colors.primary to colors.onPrimary
    } else {
        colors.surfaceVariant to colors.outline
    }

    val shape = MeetingsTheme.shapes.full

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(if (enabled && !loading) Modifier.clickable(onClick = onClick) else Modifier)
            .height(SpacingTokens.buttonHeight)
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.large),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = contentColor,
                )
            }
            Text(
                text = text,
                color = contentColor,
                style = MeetingsTheme.typography.labelLarge,
            )
        }
    }
}

/**
 * A secondary outlined button variant.
 *
 * @param text The button label text.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier applied to the button.
 * @param enabled Whether the button is enabled for interaction.
 * @param icon Optional leading icon.
 */
@Composable
fun UIKitButtonOutlined(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val colors = MeetingsTheme.colors
    val (borderColor, contentColor) = if (enabled) {
        colors.primary to colors.primary
    } else {
        colors.outline to colors.outline
    }

    val shape = MeetingsTheme.shapes.full

    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.surface)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .height(SpacingTokens.buttonHeight)
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.large),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor,
            )
        }
        Text(
            text = text,
            color = contentColor,
            style = MeetingsTheme.typography.labelLarge,
        )
    }
}
