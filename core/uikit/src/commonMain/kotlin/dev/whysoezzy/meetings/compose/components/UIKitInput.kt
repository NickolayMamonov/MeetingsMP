@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A text input field following the Meetings design system.
 *
 * @param value The current text value.
 * @param onValueChange Callback invoked when the text value changes.
 * @param modifier Modifier applied to the input.
 * @param label Optional label text displayed above the input.
 * @param placeholder Optional placeholder text displayed when the input is empty.
 * @param enabled Whether the input is enabled for interaction.
 * @param readOnly Whether the input is read-only.
 * @param singleLine Whether the input is restricted to a single line.
 * @param visualTransformation Visual transformation for the input (e.g., password).
 * @param leadingIcon Optional leading icon composable.
 * @param trailingIcon Optional trailing icon composable.
 * @param isError Whether the input is in an error state.
 * @param errorMessage Optional error message displayed below the input.
 */
@Composable
fun UIKitInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val colors = MeetingsTheme.colors
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.outline,
        errorBorderColor = colors.error,
        focusedContainerColor = colors.surface,
        unfocusedContainerColor = colors.surface,
        cursorColor = colors.primary,
        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.onSurfaceVariant,
        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
    )

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MeetingsTheme.typography.labelMedium,
                color = if (isError) colors.error else colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = SpacingTokens.micro),
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = if (placeholder != null) {
                { Text(text = placeholder, style = MeetingsTheme.typography.bodyMedium) }
            } else null,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            colors = textFieldColors,
            shape = MeetingsTheme.shapes.small,
            textStyle = MeetingsTheme.typography.bodyLarge,
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MeetingsTheme.typography.bodySmall,
                color = colors.error,
                modifier = Modifier.padding(top = SpacingTokens.micro),
            )
        }
    }
}
