@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme

/**
 * Top bar with back navigation and share action.
 *
 * @param title Title text to display.
 * @param backIcon Icon for the back button.
 * @param onBackClick Callback invoked when the back button is clicked.
 * @param shareIcon Icon for the share button.
 * @param onShareClick Callback invoked when the share button is clicked.
 * @param modifier Modifier applied to the top bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackShareTopBar(
    title: String,
    backIcon: ImageVector,
    onBackClick: () -> Unit,
    shareIcon: ImageVector,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MeetingsTheme.typography.titleLarge,
                color = MeetingsTheme.colors.onSurface,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = backIcon,
                    contentDescription = "Back",
                    tint = MeetingsTheme.colors.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = shareIcon,
                    contentDescription = "Share",
                    tint = MeetingsTheme.colors.onSurface,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MeetingsTheme.colors.surface,
            titleContentColor = MeetingsTheme.colors.onSurface,
        ),
        modifier = modifier,
    )
}
