package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import java.math.BigDecimal
import java.math.BigDecimal.TEN
import kotlin.random.Random

fun price(): Price = Price(TEN, REPUBLIC_CREDIT)
fun randomPrice(): Price = price().copy(amount = BigDecimal(Random.nextDouble(500.0, 1000.0)))