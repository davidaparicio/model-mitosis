package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import kotlin.random.Random

fun discount(): Discount = Discount(Amount(TEN), REPUBLIC_CREDIT)
fun randomDiscount(): Discount = discount().copy(amount = Amount(BigDecimal(Random.nextDouble(500.0, 1000.0))))
fun oneRepCreditDiscount(): Discount = Discount(Amount(ONE), REPUBLIC_CREDIT)