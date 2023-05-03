package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.values
import java.util.UUID.nameUUIDFromBytes

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice(), oneRepCreditDiscount())
fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice(), oneRepCreditDiscount())
fun randomFare(): Fare = Fare(comfortClass = values().random(), basePrice = randomPrice())
fun fareWithoutDiscount() = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())