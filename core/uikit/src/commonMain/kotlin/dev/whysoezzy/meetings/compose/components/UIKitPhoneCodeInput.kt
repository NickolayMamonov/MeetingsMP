@file:Suppress("LongParameterList")

package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A phone number input field with country code prefix.
 *
 * @param value The current phone number value (without country code).
 * @param onValueChange Callback invoked when the phone number changes.
 * @param modifier Modifier applied to the input.
 * @param countryCode The country code prefix (e.g., "+7").
 * @param placeholder Placeholder text for the phone number field.
 * @param enabled Whether the input is enabled.
 * @param isError Whether the input is in an error state.
 * @param errorMessage Optional error message.
 */
@Composable
fun UIKitPhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    countryCode: String = "+7",
    placeholder: String = "Phone number",
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val colors = MeetingsTheme.colors
    val focusRequester = FocusRequester()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.micro),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = countryCode,
                style = MeetingsTheme.typography.bodyLarge,
                color = colors.onSurface,
                modifier = Modifier.width(48.dp),
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(text = placeholder, style = MeetingsTheme.typography.bodyMedium)
                },
                enabled = enabled,
                singleLine = true,
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    errorBorderColor = colors.error,
                    cursorColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                ),
                shape = MeetingsTheme.shapes.small,
                textStyle = MeetingsTheme.typography.bodyLarge,
                visualTransformation = VisualTransformation.None,
            )
        }

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

/**
 * A verification code input with individual digit fields.
 *
 * @param value The current code value.
 * @param onValueChange Callback invoked when the code changes.
 * @param modifier Modifier applied to the input.
 * @param codeLength The number of digits in the verification code.
 * @param enabled Whether the input is enabled.
 * @param isError Whether the input is in an error state.
 * @param errorMessage Optional error message.
 */
@Composable
fun UIKitCodeInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val colors = MeetingsTheme.colors

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
        ) {
            repeat(codeLength) { index ->
                val digit = if (index < value.length) value[index].toString() else ""
                val digitColor = if (digit.isNotEmpty()) colors.onSurface else colors.outline
                val borderColor = when {
                    isError -> colors.error
                    digit.isNotEmpty() -> colors.primary
                    else -> colors.outline
                }

                OutlinedTextField(
                    value = digit,
                    onValueChange = { newDigit ->
                        if (newDigit.length <= 1 && newDigit.all { it.isDigit() }) {
                            val newValue = StringBuilder(value)
                            if (newDigit.isEmpty() && index < value.length) {
                                newValue.deleteCharAt(index)
                            } else if (newDigit.isNotEmpty()) {
                                while (newValue.length <= index) newValue.append(' ')
                                newValue[index] = newDigit[0]
                            }
                            onValueChange(newValue.toString().trimEnd())
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = enabled,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = borderColor,
                        unfocusedBorderColor = borderColor,
                        errorBorderColor = colors.error,
                        cursorColor = colors.primary,
                        focusedTextColor = digitColor,
                        unfocusedTextColor = digitColor,
                    ),
                    shape = MeetingsTheme.shapes.small,
                    textStyle = MeetingsTheme.typography.headlineMedium,
                )
            }
        }

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
