package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Amount
import java.math.BigDecimal
import java.math.BigDecimal.TEN
import kotlin.random.Random

fun amount(): Amount = Amount(TEN)
fun amount(value: Int): Amount = Amount(BigDecimal(value))
fun amount(value: Double): Amount = Amount(BigDecimal(value))
fun amount(value: String): Amount = Amount(BigDecimal(value))
fun randomAmount(): Amount = Amount(BigDecimal(Random.nextDouble(500.0, 1000.0)))