package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
) {

    infix fun isCompatibleWith(seatLocation: SeatLocation) : Boolean {
        return seatLocation in comfortClass.compatibleSeatLocations
    }
}

operator fun BigDecimal.div(value: BigDecimal) : BigDecimal = this.divide(value, 50, RoundingMode.HALF_UP)