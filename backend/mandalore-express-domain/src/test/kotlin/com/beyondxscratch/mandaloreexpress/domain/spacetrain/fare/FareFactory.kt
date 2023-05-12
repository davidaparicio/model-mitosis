package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.values
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.FareOption
import java.util.UUID.nameUUIDFromBytes

fun fare(): FareOption = FareOption(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())
fun firstClassFare() = fare()
fun secondClassFare() = FareOption(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomFare(): FareOption = FareOption(comfortClass = values().random(), price = randomPrice())