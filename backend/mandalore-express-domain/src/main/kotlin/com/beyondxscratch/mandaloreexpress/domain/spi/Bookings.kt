package com.beyondxscratch.mandaloreexpress.domain.spi

import com.beyondxscratch.mandaloreexpress.domain.Booking
import java.util.UUID

interface Bookings {
    infix fun `find booking identified by`(bookingId: UUID): Booking?
    infix fun save(booking: Booking): Booking
}