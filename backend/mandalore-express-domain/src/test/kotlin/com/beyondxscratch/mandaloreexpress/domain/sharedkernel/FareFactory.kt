package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.ComfortClass.values
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, price())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, price())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())