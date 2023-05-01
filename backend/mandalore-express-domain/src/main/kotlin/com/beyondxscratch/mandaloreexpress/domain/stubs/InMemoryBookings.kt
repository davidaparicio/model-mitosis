package com.beyondxscratch.mandaloreexpress.domain.stubs

import com.beyondxscratch.mandaloreexpress.domain.Booking
import com.beyondxscratch.mandaloreexpress.domain.spi.Bookings
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