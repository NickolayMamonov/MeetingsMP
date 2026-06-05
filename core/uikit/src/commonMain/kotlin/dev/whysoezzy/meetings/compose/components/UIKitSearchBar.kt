@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
 * A search bar component following the Meetings design system.
 *
 * @param value The current search query text.
 * @param onValueChange Callback invoked when the search query changes.
 * @param modifier Modifier applied to the search bar.
 * @param placeholder Placeholder text displayed when the search bar is empty.
 * @param onSearch Callback invoked when the user submits a search.
 * @param enabled Whether the search bar is enabled for interaction.
 * @param searchIcon The icon to display as the search indicator.
 */
@Suppress("UnusedParameter")
@Composable
fun UIKitSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    onSearch: (() -> Unit)? = null,
    enabled: Boolean = true,
    searchIcon: ImageVector? = null,
) {
    val colors = MeetingsTheme.colors
    val shape = MeetingsTheme.shapes.large

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surfaceVariant)
            .padding(horizontal = SpacingTokens.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
    ) {
        if (searchIcon != null) {
            Icon(
                imageVector = searchIcon,
                contentDescription = "Search",
                modifier = Modifier.size(20.dp),
                tint = colors.onSurfaceVariant,
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MeetingsTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                )
            },
            enabled = enabled,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outlineVariant,
                cursorColor = colors.primary,
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
            ),
            shape = MeetingsTheme.shapes.small,
            textStyle = MeetingsTheme.typography.bodyLarge,
        )
    }
}
