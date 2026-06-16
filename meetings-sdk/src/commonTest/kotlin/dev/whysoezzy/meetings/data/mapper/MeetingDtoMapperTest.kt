package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.MeetingStatus
import dev.whysoezzy.meetingssdk.models.dto.CommunityHostDto
import dev.whysoezzy.meetingssdk.models.dto.MeetingAddressDto
import dev.whysoezzy.meetingssdk.models.dto.MeetingDto
import dev.whysoezzy.meetingssdk.models.dto.MeetingTagDto
import dev.whysoezzy.meetingssdk.models.dto.PersonHostDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class MeetingDtoMapperTest {

    private val internalMeetingDto = MeetingDto(
        id = 1L,
        imageUrl = "https://example.com/image.png",
        title = "Kotlin Meetup",
        description = "A meetup about Kotlin",
        time = 1718668800000L,
        date = "2024-06-18",
        address = MeetingAddressDto(
            address = "Moscow, Tverskaya 1",
            latitude = 55.7558,
            longitude = 37.6173,
        ),
        tags = listOf(MeetingTagDto(id = 10L, text = "Kotlin")),
        personHost = PersonHostDto(
            id = 100L,
            name = "Ivan",
            surname = "Petrov",
            description = "Kotlin developer",
            imageUrl = "https://example.com/ivan.png",
        ),
        communityHost = null,
        participants = emptyList(),
        meetingStatus = "ACTIVE",
        isUserInParticipants = false,
        capacity = 50,
        source = "INTERNAL",
        externalUrl = null,
        isOnline = false,
    )

    private val timepadMeetingDto = MeetingDto(
        id = 2L,
        imageUrl = "https://timepad.ru/image.png",
        title = "Timepad Conference",
        description = "External conference",
        time = 1718755200000L,
        date = "2024-06-19",
        address = MeetingAddressDto(
            address = "",
            latitude = 0.0,
            longitude = 0.0,
        ),
        tags = listOf(MeetingTagDto(id = 20L, text = "Conference")),
        personHost = null,
        communityHost = CommunityHostDto(
            id = 200L,
            title = "Timepad Org",
            description = "Organizer",
            imageUrl = "https://timepad.ru/org.png",
            meetingsInfo = emptyList(),
        ),
        participants = emptyList(),
        meetingStatus = "ACTIVE",
        isUserInParticipants = false,
        capacity = 0,
        source = "TIMEPAD",
        externalUrl = "https://timepad.ru/event/123",
        isOnline = true,
    )

    @Test
    fun meetingDto_toDomain_mapsInternalMeetingCorrectly() {
        val meeting = internalMeetingDto.toDomain()

        assertEquals(1L, meeting.id)
        assertEquals("Kotlin Meetup", meeting.title)
        assertEquals("Moscow, Tverskaya 1", meeting.address.address)
        assertEquals(55.7558, meeting.address.latitude)
        assertEquals(37.6173, meeting.address.longitude)
        assertEquals(MeetingStatus.ACTIVE, meeting.meetingStatus)
        assertEquals(50, meeting.capacity)
        assertEquals("INTERNAL", meeting.source)
        assertNull(meeting.externalUrl)
        assertFalse(meeting.isOnline)
    }

    @Test
    fun meetingDto_toDomain_internalMeeting_hasLocation() {
        val meeting = internalMeetingDto.toDomain()

        assertTrue(meeting.hasLocation, "Internal meeting with coordinates should have location")
    }

    @Test
    fun meetingDto_toDomain_timepadMeeting_mapsCorrectly() {
        val meeting = timepadMeetingDto.toDomain()

        assertEquals(2L, meeting.id)
        assertEquals("Timepad Conference", meeting.title)
        assertEquals("TIMEPAD", meeting.source)
        assertEquals("https://timepad.ru/event/123", meeting.externalUrl)
        assertTrue(meeting.isOnline)
        assertEquals(0, meeting.capacity)
    }

    @Test
    fun meetingDto_toDomain_timepadOnlineMeeting_noLocation() {
        val meeting = timepadMeetingDto.toDomain()

        assertFalse(meeting.hasLocation, "Online meeting should not have location")
    }

    @Test
    fun meetingDto_toDomain_timepadMeeting_nullHostAndEmptyParticipants() {
        val meeting = timepadMeetingDto.toDomain()

        assertNull(meeting.personHost, "TIMEPAD meeting should have null personHost")
        assertTrue(meeting.participants.isEmpty(), "TIMEPAD meeting should have empty participants")
    }

    @Test
    fun meetingDto_toDomain_offlineMeetingWithoutCoordinates_noLocation() {
        val offlineNoCoords = internalMeetingDto.copy(
            address = MeetingAddressDto(
                address = "Online event",
                latitude = 0.0,
                longitude = 0.0,
            ),
            isOnline = false,
        )

        val meeting = offlineNoCoords.toDomain()

        assertFalse(meeting.hasLocation, "Offline meeting without coordinates should not have location")
    }

    @Test
    fun meetingDto_toDomain_onlineMeetingWithCoordinates_noLocation() {
        val onlineWithCoords = internalMeetingDto.copy(
            isOnline = true,
        )

        val meeting = onlineWithCoords.toDomain()

        assertFalse(meeting.hasLocation, "Online meeting should not show location even with coordinates")
    }

    @Test
    fun meetingDto_defaults_sourceIsInternal() {
        val dto = MeetingDto(
            id = 3L,
            imageUrl = "",
            title = "Test",
            description = "",
            time = 0L,
            date = "",
            address = MeetingAddressDto(address = "", latitude = 0.0, longitude = 0.0),
            tags = emptyList(),
            personHost = null,
            communityHost = null,
            participants = emptyList(),
            meetingStatus = "ACTIVE",
            isUserInParticipants = false,
            capacity = null,
        )

        assertEquals("INTERNAL", dto.source, "Default source should be INTERNAL")
        assertNull(dto.externalUrl, "Default externalUrl should be null")
        assertFalse(dto.isOnline, "Default isOnline should be false")
    }

    @Test
    fun meetingDto_toDomain_capacityNull_defaultsToZero() {
        val dto = internalMeetingDto.copy(capacity = null)

        val meeting = dto.toDomain()

        assertEquals(0, meeting.capacity)
    }

    @Test
    fun meetingDto_toDomain_mapsPersonHost() {
        val meeting = internalMeetingDto.toDomain()

        assertEquals(100L, meeting.personHost!!.id)
        assertEquals("Ivan", meeting.personHost.name)
        assertEquals("Petrov", meeting.personHost.surname)
    }

    @Test
    fun meetingDto_toDomain_mapsCommunityHost() {
        val meeting = timepadMeetingDto.toDomain()

        assertEquals(200L, meeting.communityHost!!.id)
        assertEquals("Timepad Org", meeting.communityHost.title)
    }

    @Test
    fun meetingStatus_stringMapping_isCorrect() {
        assertEquals(MeetingStatus.ACTIVE, "ACTIVE".toMeetingStatus())
        assertEquals(MeetingStatus.COMPLETED, "COMPLETED".toMeetingStatus())
        assertEquals(MeetingStatus.COMPLETED, "FINISHED".toMeetingStatus())
        assertEquals(MeetingStatus.CANCELLED, "CANCELLED".toMeetingStatus())
        assertEquals(MeetingStatus.FULL, "FULL".toMeetingStatus())
        assertEquals(MeetingStatus.DRAFT, "DRAFT".toMeetingStatus())
        assertEquals(MeetingStatus.ACTIVE, "unknown".toMeetingStatus())
    }
}
