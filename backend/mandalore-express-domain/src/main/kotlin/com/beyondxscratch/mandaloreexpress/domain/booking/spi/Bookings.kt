package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import java.util.UUID

interface Bookings {
    infix fun `find booking identified by`(bookingId: UUID): Booking?
    infix fun save(booking: Booking): Booking
}