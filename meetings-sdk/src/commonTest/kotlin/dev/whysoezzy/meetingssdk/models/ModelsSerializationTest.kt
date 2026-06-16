package dev.whysoezzy.meetingssdk.models

import dev.whysoezzy.meetingssdk.models.dto.MeetingAddressDto
import dev.whysoezzy.meetingssdk.models.dto.MeetingDto
import dev.whysoezzy.meetingssdk.models.dto.MeetingTagDto
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class ModelsSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
        coerceInputValues = true
    }

    @Test
    fun refreshTokenBody_serializesCorrectly() {
        val body = RefreshTokenBody(refreshToken = "abc123")
        assertEquals("abc123", body.refreshToken)
    }

    @Test
    fun adBlock_hasCorrectDefaults() {
        val ad = AdBlock(
            type = "COMMUNITIES",
            id = "ad1",
            title = "Test Ad",
            description = "Test description",
        )
        assertEquals("COMMUNITIES", ad.type)
        assertEquals("ad1", ad.id)
        assertEquals(true, ad.isActive)
        assertNull(ad.communities)
        assertNull(ad.actionText)
        assertNull(ad.actionUrl)
        assertNull(ad.users)
    }

    @Test
    fun adBlock_withCommunities_hasCorrectData() {
        val community = CommunityShort(
            id = "c1",
            name = "Test Community",
            subscribersCount = 100,
        )
        val ad = AdBlock(
            type = "COMMUNITIES",
            id = "ad1",
            title = "Test Ad",
            description = "Test description",
            communities = listOf(community),
        )
        assertEquals(1, ad.communities!!.size)
        assertEquals("c1", ad.communities!!.first().id)
    }

    @Test
    fun errorResponse_hasCorrectFields() {
        val error = ErrorResponse(
            error = "VALIDATION_ERROR",
            message = "Invalid input",
            details = mapOf("field" to "required"),
        )
        assertEquals("VALIDATION_ERROR", error.error)
        assertEquals("Invalid input", error.message)
        assertEquals(mapOf("field" to "required"), error.details)
    }

    @Test
    fun errorResponse_detailsDefaultToNull() {
        val error = ErrorResponse(
            error = "NOT_FOUND",
            message = "Resource not found",
        )
        assertNull(error.details)
    }

    @Test
    fun paginatedResponse_hasCorrectFields() {
        val response = PaginatedResponse(
            items = listOf("a", "b"),
            cursor = "next_page",
            total = 42,
        )
        assertEquals(listOf("a", "b"), response.items)
        assertEquals("next_page", response.cursor)
        assertEquals(42, response.total)
    }

    @Test
    fun paginatedResponse_cursorDefaultsToNull() {
        val response = PaginatedResponse<String>(
            items = emptyList(),
            total = 0,
        )
        assertNull(response.cursor)
    }

    @Test
    fun meetingDto_deserialization_withoutNewFields_defaultsCorrect() {
        val jsonString = """{
            "id": 1,
            "imageUrl": "https://example.com/img.png",
            "title": "Test Meeting",
            "description": "Description",
            "time": 1718668800000,
            "date": "2024-06-18",
            "address": {"address": "Moscow", "latitude": 55.7558, "longitude": 37.6173},
            "tags": [{"id": 10, "text": "Kotlin"}],
            "personHost": null,
            "communityHost": null,
            "participants": [],
            "meetingStatus": "ACTIVE",
            "isUserInParticipants": false,
            "capacity": 50
        }""".trimIndent()

        val dto = json.decodeFromString<MeetingDto>(jsonString)

        assertEquals("INTERNAL", dto.source, "Default source should be INTERNAL")
        assertNull(dto.externalUrl, "Default externalUrl should be null")
        assertFalse(dto.isOnline, "Default isOnline should be false")
    }

    @Test
    fun meetingDto_deserialization_withTimepadFields_mapsCorrectly() {
        val jsonString = """{
            "id": 2,
            "imageUrl": "https://timepad.ru/img.png",
            "title": "Timepad Event",
            "description": "External event",
            "time": 1718755200000,
            "date": "2024-06-19",
            "address": {"address": "", "latitude": 0.0, "longitude": 0.0},
            "tags": [{"id": 20, "text": "Conference"}],
            "personHost": null,
            "communityHost": null,
            "participants": [],
            "meetingStatus": "ACTIVE",
            "isUserInParticipants": false,
            "capacity": 0,
            "source": "TIMEPAD",
            "externalUrl": "https://timepad.ru/event/123",
            "isOnline": true
        }""".trimIndent()

        val dto = json.decodeFromString<MeetingDto>(jsonString)

        assertEquals("TIMEPAD", dto.source)
        assertEquals("https://timepad.ru/event/123", dto.externalUrl)
        assertTrue(dto.isOnline)
        assertEquals(0, dto.capacity)
    }

    @Test
    fun meetingDto_deserialization_partialNewFields_defaultsMissing() {
        val jsonString = """{
            "id": 3,
            "imageUrl": "",
            "title": "Partial",
            "description": "",
            "time": 0,
            "date": "",
            "address": {"address": "", "latitude": 0.0, "longitude": 0.0},
            "tags": [],
            "personHost": null,
            "communityHost": null,
            "participants": [],
            "meetingStatus": "ACTIVE",
            "isUserInParticipants": false,
            "capacity": null,
            "source": "TIMEPAD"
        }""".trimIndent()

        val dto = json.decodeFromString<MeetingDto>(jsonString)

        assertEquals("TIMEPAD", dto.source, "Explicitly set source should be TIMEPAD")
        assertNull(dto.externalUrl, "Missing externalUrl should default to null")
        assertFalse(dto.isOnline, "Missing isOnline should default to false")
    }
}
