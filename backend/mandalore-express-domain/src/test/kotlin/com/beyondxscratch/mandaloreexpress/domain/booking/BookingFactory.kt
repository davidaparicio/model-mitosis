package com.beyondxscratch.mandaloreexpress.domain.booking

import java.util.UUID

fun booking() = Booking(id = UUID.nameUUIDFromBytes("1".toByteArray()), spaceTrains = listOf(spaceTrain(), spaceTrain()))
fun randomBooking() = Booking(spaceTrains = listOf(randomSpaceTrain()))
