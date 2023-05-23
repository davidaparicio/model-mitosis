package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation.*
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.numbered
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withFirstClass
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.NoSuchElementException

class BookingShould : EqualityShould<Booking> {
    @Test
    fun `not be created from an empty list of space trains`() {
        assertThatThrownBy { Booking(spaceTrains = listOf()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("cannot book nothing")
    }

    @Test
    fun `compute its total price`(@Random spaceTrain: SpaceTrain, @Random anotherSpaceTrain: SpaceTrain) {
        val booking = Booking(spaceTrains = listOf(spaceTrain, anotherSpaceTrain))
        assertThat(booking.totalPrice).isEqualTo(spaceTrain.fare.price + anotherSpaceTrain.fare.price)
    }

    @Test
    fun `finalize a booking`(@NonFinalized nonFinalizedBooking: Booking) {
        val finalizedBooking = nonFinalizedBooking.finalize()

        assertThat(finalizedBooking.finalized).isTrue
    }

    @Test
    fun `cannot select the seatlocation of an unknown spacetrain`(booking: Booking) {
        assertThatThrownBy { booking.selectSeatLocationFor("non-existing-number", CARGO_BAY) }
            .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun `selects the seat location ANY on every train by default`() {
        val spaceTrain = spaceTrain().numbered("42")
        val booking = Booking(spaceTrains = listOf(spaceTrain))

        assertThat(booking.selectedSeatLocations).containsEntry(spaceTrain, ANY)
    }

    @Test
    fun `can select a seatlocation compatible with the comfortClass`() {
        val spaceTrainNumber = "12"
        val firstClassLocation = FLYING_BRIDGE

        val spaceTrain = spaceTrain().numbered(spaceTrainNumber).withFirstClass()
        val booking = Booking(spaceTrains = listOf(spaceTrain))

        val bookingWithSelection = booking.selectSeatLocationFor(spaceTrainNumber, firstClassLocation)

        assertThat(bookingWithSelection.selectedSeatLocations[spaceTrain]).isEqualTo(firstClassLocation)
        assertThat(bookingWithSelection.finalized).isFalse()
    }
    @Test
    fun `not select a seatlocation incompatible with the comfortClass`() {
        val spaceTrainNumber = "12"
        val secondClassLocation = CARGO_BAY

        val spaceTrain = spaceTrain().numbered(spaceTrainNumber).withFirstClass()
        val booking = Booking(spaceTrains = listOf(spaceTrain))

        assertThatThrownBy { booking.selectSeatLocationFor(spaceTrainNumber, secondClassLocation) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SelectedSeatLocations are incompatible with the SpaceTrains")
    }
}