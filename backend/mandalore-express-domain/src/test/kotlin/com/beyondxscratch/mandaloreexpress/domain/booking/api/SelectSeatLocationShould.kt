package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.Finalized
import com.beyondxscratch.mandaloreexpress.domain.booking.NonFinalized
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation.*
import com.beyondxscratch.mandaloreexpress.domain.booking.havingSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.numbered
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

interface SelectSeatLocationShould {
    val bookings: Bookings

    val selectSeatLocation: SelectSeatLocation

    @Test
    fun `select a seatlocation`(@NonFinalized booking: Booking) {
        bookings.save(booking)
        val spaceTrain = booking.spaceTrains.first()
        val spaceTrainNumber = spaceTrain.number


        val bookingWithSelection = selectSeatLocation select FLYING_BRIDGE `on spacetrain` spaceTrainNumber `on booking` booking.id

        assertThat(bookingWithSelection.selectedSeatLocations[spaceTrain]).isEqualTo(FLYING_BRIDGE)

        val savedBooking = bookings `find booking identified by` bookingWithSelection.id
        assertThat(savedBooking!!.selectedSeatLocations[spaceTrain]).isEqualTo(FLYING_BRIDGE)
    }

    @Test
    fun `not select a seatlocation on a finalized booking`(@Finalized finalizedBooking: Booking) {
        val spaceTrainNumber = "54"
        bookings.save(finalizedBooking.havingSpaceTrains(spaceTrain().numbered(spaceTrainNumber)))


        assertThatThrownBy { selectSeatLocation select ANY `on spacetrain` spaceTrainNumber `on booking` finalizedBooking.id }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Cannot do a seatLocation selection on a finalized booking")
    }
}