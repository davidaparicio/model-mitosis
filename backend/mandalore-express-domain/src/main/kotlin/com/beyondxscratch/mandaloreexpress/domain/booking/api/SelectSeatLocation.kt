package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation
import java.util.*

interface SelectSeatLocation {

    fun selectSeatLocationOnBooking(bookingId: UUID, spaceTrainNumber: String, seatLocation: SeatLocation): Booking

    infix fun select(seatLocation: SeatLocation): OnSpaceTrain =
        { spaceTrainNumber: String -> this.onSpaceTrain(spaceTrainNumber, seatLocation) }

    private fun onSpaceTrain(spaceTrainId: String, seatLocation: SeatLocation): OnBooking =
        { bookingId: UUID -> this.selectSeatLocationOnBooking(bookingId, spaceTrainId, seatLocation) }
}

typealias OnSpaceTrain = (String) -> OnBooking

infix fun OnSpaceTrain.`on spacetrain`(spaceTrainNumber: String): OnBooking = this.invoke(spaceTrainNumber)


typealias OnBooking = (UUID) -> Booking

infix fun OnBooking.`on booking`(bookingId: UUID): Booking = this.invoke(bookingId)