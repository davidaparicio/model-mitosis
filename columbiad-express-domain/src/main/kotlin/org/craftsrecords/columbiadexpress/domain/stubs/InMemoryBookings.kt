package org.craftsrecords.columbiadexpress.domain.stubs

import org.craftsrecords.columbiadexpress.domain.booking.Booking
import org.craftsrecords.columbiadexpress.domain.spi.Bookings
import java.util.UUID

@Stub
class InMemoryBookings : Bookings {

    val bookings: MutableMap<UUID, Booking> = hashMapOf()

    override fun `find booking identified by`(bookingId: UUID): Booking? = bookings[bookingId]

    override fun save(booking: Booking): Booking {
        bookings[booking.id] = booking
        return booking
    }
}