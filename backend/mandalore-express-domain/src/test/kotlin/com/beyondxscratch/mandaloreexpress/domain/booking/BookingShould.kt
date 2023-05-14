package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation.*
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.numbered
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withFirstClass
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
    fun `not be created with selectedSeatLocations incompatible with the SpaceTrains`() {
        val spaceTrainNumber = "42"
        assertThatThrownBy {

            val spacetrain = spaceTrain().numbered(spaceTrainNumber).withFirstClass()
            Booking(
                spaceTrains = listOf(spacetrain),
                selectedSeatLocations = mapOf(spacetrain to CARGO_BAY)
            )
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SelectedSeatLocations are incompatible with the SpaceTrains")
    }

    @Test
    fun `not select a seatlocation incompatible with the SpaceTrain`() {
        val spaceTrainNumber = "42"

        val spacetrain = spaceTrain().numbered(spaceTrainNumber).withFirstClass()
        val booking = Booking(
            spaceTrains = listOf(spacetrain)
        )

        assertThatThrownBy { booking.selectSeatLocationFor(spaceTrainNumber, CARGO_BAY) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SelectedSeatLocations are incompatible with the SpaceTrains")
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
    fun `selects the seat location ANY on every train by default`(baseBooking: Booking) {
        val spaceTrain = spaceTrain().numbered("42")
        val booking = baseBooking.havingSpaceTrains(spaceTrain)

        assertThat(booking.selectedSeatLocations[spaceTrain]).isEqualTo(ANY)
    }

    @Test
    fun `can select a seatlocation compatible with the comfortClass`(@NonFinalized baseBooking: Booking) {

        val spaceTrain = spaceTrain().numbered("12").withFirstClass()
        val nonFinalizedBooking =
            baseBooking.havingSpaceTrains(
                spaceTrain
            )

        val bookingWithSelection = nonFinalizedBooking.selectSeatLocationFor("12", FLYING_BRIDGE)

        assertThat(bookingWithSelection.selectedSeatLocations[spaceTrain]).isEqualTo(FLYING_BRIDGE)
        assertThat(bookingWithSelection.finalized).isFalse()
    }
}