package com.beyondxscratch.mandaloreexpress.domain.search.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Bookings

interface BookSpaceTrains {
    val bookings: Bookings
    infix fun `from the selection of`(search: Search): Booking
}