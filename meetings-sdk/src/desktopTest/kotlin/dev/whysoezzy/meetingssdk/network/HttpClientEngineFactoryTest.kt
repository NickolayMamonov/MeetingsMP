package dev.whysoezzy.meetingssdk.network

import kotlin.test.Test
import kotlin.test.assertNotNull

@Suppress("FunctionNaming")
class HttpClientEngineFactoryTest {

    @Test
    fun createHttpClientEngine_withoutPins_returnsOkHttpEngine() {
        val engine = createHttpClientEngine(pins = null)
        assertNotNull(engine)
    }

    @Test
    fun createHttpClientEngine_withEmptyPins_returnsOkHttpEngine() {
        val pins = CertificatePins()
        val engine = createHttpClientEngine(pins = pins)
        assertNotNull(engine)
    }

    @Test
    fun createHttpClientEngine_withPins_returnsOkHttpEngine() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf("sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            )
        )
        val engine = createHttpClientEngine(pins = pins)
        assertNotNull(engine)
    }

    @Test
    fun createHttpClientEngine_withEnforcePinsFalse_returnsOkHttpEngine() {
        val pins = CertificatePins(
            pins = mapOf(
                "api.meetings.mp" to listOf("sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            ),
            enforcePins = false
        )
        val engine = createHttpClientEngine(pins = pins)
        assertNotNull(engine)
    }
}
