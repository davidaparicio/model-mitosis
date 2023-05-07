package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.*
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.randomPrice
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.tenRepCreditsPrice
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), ComfortClass.FIRST, tenRepCreditsPrice())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), ComfortClass.SECOND, tenRepCreditsPrice())
fun randomFare(): Fare = Fare(comfortClass = ComfortClass.values().random(), price = randomPrice())