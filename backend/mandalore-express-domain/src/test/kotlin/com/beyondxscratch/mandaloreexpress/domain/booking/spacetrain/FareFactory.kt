package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass.values
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Fare
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())