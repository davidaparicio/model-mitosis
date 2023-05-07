package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass.*
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency
import java.math.BigDecimal
import java.util.UUID.nameUUIDFromBytes
import kotlin.random.Random

fun fare(): Fare = Fare(nameUUIDFromBytes("fare1".toByteArray()), FIRST, tenRepCreditsPrice())

fun firstClassFare() = fare()
fun secondClassFare() = Fare(nameUUIDFromBytes("fare2".toByteArray()), SECOND, tenRepCreditsPrice())
fun randomFare(): Fare = Fare(comfortClass = values().random(), price = randomPrice())

inline fun <reified T> tenRepCreditsPrice() : T{
    return T::class.constructors.first().call(Amount(BigDecimal.TEN), Currency.REPUBLIC_CREDIT)
}

inline fun <reified T> randomPrice() : T{
    return T::class.constructors.first().call(Amount(Random.nextLong(100L, 200L).toBigDecimal()), Currency.REPUBLIC_CREDIT)
}