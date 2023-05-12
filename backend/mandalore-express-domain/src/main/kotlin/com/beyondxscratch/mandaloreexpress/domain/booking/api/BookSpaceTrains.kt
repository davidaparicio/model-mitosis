package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.search.Search

interface BookSpaceTrains {
    val bookings: Bookings
    infix fun `from the selection of`(search: Search): Booking
}