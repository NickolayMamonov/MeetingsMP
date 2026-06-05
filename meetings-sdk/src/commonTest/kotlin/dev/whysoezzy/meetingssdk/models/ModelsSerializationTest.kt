package dev.whysoezzy.meetingssdk.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Suppress("FunctionNaming")
class ModelsSerializationTest {

    @Test
    fun refreshTokenBody_serializesCorrectly() {
        val body = RefreshTokenBody(token = "abc123")
        assertEquals("abc123", body.token)
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
}
