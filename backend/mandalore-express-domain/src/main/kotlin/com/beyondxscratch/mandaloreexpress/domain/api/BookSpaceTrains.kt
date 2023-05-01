package com.beyondxscratch.mandaloreexpress.domain.api

import com.beyondxscratch.mandaloreexpress.domain.Search
import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.spi.Bookings

interface BookSpaceTrains {
    val bookings: Bookings
    infix fun `from the selection of`(search: Search): Booking
}