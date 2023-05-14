package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import java.util.UUID

interface FinalizeBooking {
    infix fun with(bookingId: UUID) : Booking
}