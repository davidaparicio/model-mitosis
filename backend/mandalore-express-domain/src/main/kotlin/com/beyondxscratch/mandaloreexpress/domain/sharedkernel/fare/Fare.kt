package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation
import java.util.*

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
) {
    infix fun isCompatibleWith(seatLocation: SeatLocation): Boolean {
        return seatLocation in comfortClass.compatibleSeatLocations
    }
}
typealias Fares = Set<Fare>