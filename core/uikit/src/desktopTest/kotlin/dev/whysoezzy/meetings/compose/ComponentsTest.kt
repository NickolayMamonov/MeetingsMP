package dev.whysoezzy.meetings.compose

import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Basic smoke tests for UIKit components.
 * Verifies that component classes and functions exist and can be referenced.
 */
class ComponentsTest {

    @Test
    fun tokenObjectsAreAccessible() {
        val spacingTokens = dev.whysoezzy.meetings.compose.tokens.SpacingTokens
        assertNotNull(spacingTokens)
        assertTrue(spacingTokens.medium > spacingTokens.micro)
    }
}
