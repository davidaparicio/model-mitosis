package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.randomSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.spaceTrain
import java.util.UUID

fun booking() = Booking(id = UUID.nameUUIDFromBytes("1".toByteArray()), spaceTrains = listOf(spaceTrain(), spaceTrain()))
fun randomBooking() = Booking(spaceTrains = listOf(randomSpaceTrain()))
