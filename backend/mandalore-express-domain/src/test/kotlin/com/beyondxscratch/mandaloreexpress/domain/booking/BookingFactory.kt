package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.randomSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import java.util.*

fun booking() =
    Booking(id = UUID.nameUUIDFromBytes("1".toByteArray()), spaceTrains = listOf(spaceTrain(), spaceTrain()))

fun finalizedBooking() = Booking(
    id = UUID.nameUUIDFromBytes("1".toByteArray()),
    spaceTrains = listOf(spaceTrain(), spaceTrain()),
    finalized = true
)

fun nonFinalizedBooking() = booking()

fun randomBooking() = Booking(spaceTrains = listOf(randomSpaceTrain()))

fun Booking.havingSpaceTrains(vararg spaceTrains: SpaceTrain): Booking {
    return this.copy(
        spaceTrains = spaceTrains.asList())
}