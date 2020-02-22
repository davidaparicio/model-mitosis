package org.craftsrecords.columbiadexpress.domain.spi

import org.craftsrecords.columbiadexpress.domain.booking.Booking
import java.util.UUID

interface Bookings {
    infix fun `find booking identified by`(bookingId: UUID): Booking?
    infix fun save(booking: Booking): Booking
}