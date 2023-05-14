package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.Finalized
import com.beyondxscratch.mandaloreexpress.domain.booking.NonFinalized
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException
import java.util.UUID.randomUUID

interface FinalizeBookingShould {
    val bookings: Bookings

    val finalizeBooking: FinalizeBooking

    @Test
    fun `cannot finalize a booking that does not exist`() {
        val unknownId = randomUUID()
        assertThatThrownBy { finalizeBooking with unknownId }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown booking $unknownId")
    }

    @Test
    fun `finalize booking`(@NonFinalized booking: Booking) {
        bookings.save(booking)

        val finalizedBooking = finalizeBooking with booking.id
        val savedBooking = bookings `find booking identified by` booking.id

        assertThat(finalizedBooking.finalized).isTrue
        assertThat(savedBooking?.finalized).isTrue
        assertThat(finalizedBooking.spaceTrains).isEqualTo(savedBooking?.spaceTrains).isEqualTo(booking.spaceTrains)
        assertThat(finalizedBooking.totalPrice).isEqualTo(savedBooking?.totalPrice).isEqualTo(booking.totalPrice)
    }

    @Test
    fun `cannot finalize a booking that was already finalized`(@Finalized finalizedBooking: Booking) {
        bookings.save(finalizedBooking)

        assertThatThrownBy { finalizeBooking with finalizedBooking.id }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Booking ${finalizedBooking.id} is already finalized")
    }
}