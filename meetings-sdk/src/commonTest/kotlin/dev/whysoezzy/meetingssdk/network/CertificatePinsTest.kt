package dev.whysoezzy.meetingssdk.network

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class CertificatePinsTest {

    @Test
    fun certificatePins_isEmpty_whenNoPinsConfigured() {
        val pins = CertificatePins()
        assertTrue(pins.isEmpty)
        assertTrue(pins.pins.isEmpty())
    }

    @Test
    fun certificatePins_isNotEmpty_whenPinsConfigured() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf("sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            )
        )
        assertEquals(false, pins.isEmpty)
        assertEquals(1, pins.pins.size)
    }

    @Test
    fun pinsFor_returnsPinsForKnownHost() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf(
                    "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
                    "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="
                )
            )
        )
        val hostPins = pins.pinsFor("api.meetings.mp")
        assertEquals(2, hostPins.size)
        assertEquals("sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=", hostPins[0])
        assertEquals("sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=", hostPins[1])
    }

    @Test
    fun pinsFor_returnsEmptyListForUnknownHost() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf("sha256/AAAA=")
            )
        )
        val hostPins = pins.pinsFor("unknown.example.com")
        assertTrue(hostPins.isEmpty())
    }

    @Test
    fun certificatePins_enforcePins_defaultsToTrue() {
        val pins = CertificatePins()
        assertEquals(true, pins.enforcePins)
    }

    @Test
    fun certificatePins_enforcePins_canBeSetToFalse() {
        val pins = CertificatePins(enforcePins = false)
        assertEquals(false, pins.enforcePins)
    }

    @Test
    fun certificatePins_multipleHosts() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf("sha256/AAAA="),
                "cdn.meetings.mp" to listOf("sha256/BBBB=")
            )
        )
        assertEquals(2, pins.pins.size)
        assertEquals(1, pins.pinsFor("api.meetings.mp").size)
        assertEquals(1, pins.pinsFor("cdn.meetings.mp").size)
    }
}
