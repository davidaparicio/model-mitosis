package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass.values
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.randomPrice
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.tenRepCreditsPrice
import java.util.UUID.nameUUIDFromBytes

fun selectedFare(): SelectedFare = SelectedFare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())
fun firstClassSelectedFare() = selectedFare()
fun secondClassSelectedFare() = SelectedFare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomSelectedFare(): SelectedFare = SelectedFare(comfortClass = values().random(), price = randomPrice())