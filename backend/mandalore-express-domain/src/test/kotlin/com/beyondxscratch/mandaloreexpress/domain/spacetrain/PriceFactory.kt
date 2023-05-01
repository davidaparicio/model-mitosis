package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import java.math.BigDecimal
import java.math.BigDecimal.TEN
import java.util.Currency.getInstance
import java.util.Locale.FRANCE
import kotlin.random.Random

fun price(): Price = Price(TEN, getInstance(FRANCE))
fun randomPrice(): Price = price().copy(amount = BigDecimal(Random.nextDouble(500.0, 1000.0)))