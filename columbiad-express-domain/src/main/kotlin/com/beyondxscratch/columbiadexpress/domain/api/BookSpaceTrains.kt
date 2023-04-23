package com.beyondxscratch.columbiadexpress.domain.api

import com.beyondxscratch.columbiadexpress.domain.booking.Booking
import com.beyondxscratch.columbiadexpress.domain.search.Search
import com.beyondxscratch.columbiadexpress.domain.spi.Bookings

interface BookSpaceTrains {
    val bookings: Bookings
    infix fun `from the selection of`(search: Search): Booking
}