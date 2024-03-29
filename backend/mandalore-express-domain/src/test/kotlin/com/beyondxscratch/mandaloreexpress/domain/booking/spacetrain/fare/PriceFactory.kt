package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.money.Amount
import com.beyondxscratch.mandaloreexpress.domain.money.Currency.CALAMARI_FLAN
import com.beyondxscratch.mandaloreexpress.domain.money.Currency.REPUBLIC_CREDIT
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import kotlin.random.Random

fun price(): Price = Price(Amount(TEN), REPUBLIC_CREDIT)
fun randomPrice(): Price = price().copy(amount = Amount(BigDecimal(Random.nextDouble(500.0, 1000.0))))

fun tenRepCreditsPrice(): Price = Price(Amount(TEN), REPUBLIC_CREDIT)
fun oneRepCreditPrice(): Price = Price(Amount(ONE), REPUBLIC_CREDIT)
fun oneCalamariFlanPrice(): Price = Price(Amount(ONE), CALAMARI_FLAN)
