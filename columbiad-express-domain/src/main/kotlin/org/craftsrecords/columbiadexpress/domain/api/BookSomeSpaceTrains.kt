package org.craftsrecords.columbiadexpress.domain.api

import org.craftsrecords.columbiadexpress.domain.booking.Booking
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.spi.Bookings

interface BookSomeSpaceTrains {
    val bookings: Bookings
    infix fun `from the selection of`(search: Search): Booking
}