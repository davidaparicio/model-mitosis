package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass.values
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())