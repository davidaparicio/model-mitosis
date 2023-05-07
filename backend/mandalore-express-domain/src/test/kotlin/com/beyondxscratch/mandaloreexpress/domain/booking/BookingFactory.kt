package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.randomSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import java.util.UUID

fun booking() = Booking(id = UUID.nameUUIDFromBytes("1".toByteArray()), spaceTrains = listOf(spaceTrain(), spaceTrain()))
fun randomBooking() = Booking(spaceTrains = listOf(randomSpaceTrain()))
