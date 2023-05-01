package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.randomSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.spaceTrain
import java.util.UUID

fun booking() = Booking(id = UUID.nameUUIDFromBytes("1".toByteArray()), spaceTrains = listOf(spaceTrain(), spaceTrain()))
fun randomBooking() = Booking(spaceTrains = listOf(randomSpaceTrain()))
