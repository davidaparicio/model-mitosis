package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.ComfortClass.values
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, price())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, price())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())