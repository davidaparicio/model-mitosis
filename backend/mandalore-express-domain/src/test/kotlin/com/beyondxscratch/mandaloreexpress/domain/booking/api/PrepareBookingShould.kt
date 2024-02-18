package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.CannotBookAPartialSelection
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.*
import java.util.UUID.randomUUID

interface PrepareBookingShould {
    val prepareBooking: PrepareBooking
    val completeSelectionSearchId: UUID
    val uncompleteSelectionSearchId: UUID
    val selectedSpaceTrains: List<SpaceTrain>

    @Test
    fun `prepare booking from the selection of`() {

        val booking = prepareBooking `from the selection of` completeSelectionSearchId

        assertThat(booking.spaceTrains).isEqualTo(selectedSpaceTrains)
        assertThat(booking.finalized).isFalse
    }

    @Test
    fun `not prepare booking from an incomplete selection`() {

        assertThatThrownBy {
            prepareBooking `from the selection of` uncompleteSelectionSearchId
        }.isInstanceOf(CannotBookAPartialSelection::class.java)
            .hasMessage("cannot book a partial selection")
    }

    @Test
    fun `throw NoSuchElementException when trying book an unknown search`() {

        val unknownId = randomUUID()
        assertThatThrownBy {
            prepareBooking `from the selection of` unknownId
        }.isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown search $unknownId")
    }
}