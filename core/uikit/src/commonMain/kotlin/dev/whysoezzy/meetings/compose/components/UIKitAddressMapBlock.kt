package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.whysoezzy.meetings.compose.models.UIKitAddress
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A block displaying an address with a placeholder for a map view.
 *
 * @param address The address to display.
 * @param modifier Modifier applied to the block.
 * @param onMapClick Callback invoked when the map placeholder is clicked.
 */
@Suppress("UnusedParameter")
@Composable
fun UIKitAddressMapBlock(
    address: UIKitAddress,
    modifier: Modifier = Modifier,
    onMapClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.medium),
    ) {
        Text(
            text = "Location",
            style = MeetingsTheme.typography.titleMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        Text(
            text = address.address,
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        // Map placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SpacingTokens.xxlarge)
                .clip(MeetingsTheme.shapes.medium)
                .background(MeetingsTheme.colors.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "${address.latitude}, ${address.longitude}",
                style = MeetingsTheme.typography.bodySmall,
                color = MeetingsTheme.colors.onSurfaceVariant,
            )
        }
    }
}
