package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.values
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.randomPrice
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.tenRepCreditsPrice
import java.util.UUID.nameUUIDFromBytes

fun fareOption(): FareOption = FareOption(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())
fun firstClassFareOption() = fareOption()
fun secondClassFareOption() = FareOption(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomFareOption(): FareOption = FareOption(comfortClass = values().random(), price = randomPrice())