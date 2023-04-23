package com.beyondxscratch.columbiadexpress.domain.sharedkernel

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.ComfortClass.FIRST
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.ComfortClass.SECOND
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.ComfortClass.values
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, price())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, price())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())