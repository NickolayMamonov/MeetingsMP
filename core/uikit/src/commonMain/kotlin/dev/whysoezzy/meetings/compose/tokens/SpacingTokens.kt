package dev.whysoezzy.meetings.compose.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design tokens for spacing values used across the Meetings app.
 *
 * Follows an 8 dp base grid with quarter-step and half-step variants
 * for fine-grained layout control.
 */
object SpacingTokens {

    // ── base grid ────────────────────────────────────────────────
    val space0: Dp = 0.dp
    val space2: Dp = 2.dp
    val space4: Dp = 4.dp
    val space6: Dp = 6.dp
    val space8: Dp = 8.dp
    val space10: Dp = 10.dp
    val space12: Dp = 12.dp
    val space14: Dp = 14.dp
    val space16: Dp = 16.dp
    val space18: Dp = 18.dp
    val space20: Dp = 20.dp
    val space24: Dp = 24.dp
    val space28: Dp = 28.dp
    val space32: Dp = 32.dp
    val space36: Dp = 36.dp
    val space40: Dp = 40.dp
    val space44: Dp = 44.dp
    val space48: Dp = 48.dp
    val space56: Dp = 56.dp
    val space64: Dp = 64.dp
    val space72: Dp = 72.dp
    val space80: Dp = 80.dp
    val space96: Dp = 96.dp

    // ── semantic aliases ─────────────────────────────────────────
    val none: Dp = space0
    val micro: Dp = space2
    val xsmall: Dp = space4
    val small: Dp = space8
    val medium: Dp = space12
    val large: Dp = space16
    val xlarge: Dp = space24
    val xxlarge: Dp = space32
    val xxxlarge: Dp = space48

    // ── component specific ───────────────────────────────────────
    val cardPadding: Dp = space12
    val buttonHeight: Dp = space44
    val iconButtonSize: Dp = space40
    val avatarSize: Dp = space40
    val avatarSizeSmall: Dp = space24
    val avatarSizeLarge: Dp = space56
    val chipHeight: Dp = space28
}
