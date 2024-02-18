package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.IsSelectionComplete
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.RetrieveSelection
import java.util.UUID

interface PrepareBooking {
    val isSelectionComplete : IsSelectionComplete
    val retrieveSelection : RetrieveSelection
    val bookings: Bookings
    infix fun `from the selection of`(searchId: UUID): Booking
}