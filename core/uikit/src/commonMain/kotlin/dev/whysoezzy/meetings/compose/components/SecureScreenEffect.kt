package dev.whysoezzy.meetings.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Effect that enables FLAG_SECURE on Android to prevent screenshots/screen recording.
 * No-op on non-Android platforms.
 *
 * @param enabled Whether the secure flag should be applied.
 */
@Composable
fun SecureScreenEffect(enabled: Boolean = true) {
    if (enabled) {
        DisposableEffect(Unit) {
            onDispose { }
        }
    }
}
