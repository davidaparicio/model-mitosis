package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import com.beyondxscratch.mandaloreexpress.domain.money.Amount
import com.beyondxscratch.mandaloreexpress.domain.money.Currency
import java.math.BigDecimal
import kotlin.random.Random

fun taxPortion(): TaxPortion = TaxPortion(Amount(BigDecimal.TEN), Currency.REPUBLIC_CREDIT)
fun randomTaxPortion(): TaxPortion = taxPortion().copy(amount = Amount(BigDecimal(Random.nextDouble(500.0, 1000.0))))

fun tenRepCreditsTaxPortion(): TaxPortion = TaxPortion(Amount(BigDecimal.TEN), Currency.REPUBLIC_CREDIT)
fun oneRepCreditTaxPortion(): TaxPortion = TaxPortion(Amount(BigDecimal.ONE), Currency.REPUBLIC_CREDIT)
fun oneCalamariFlanTaxPortion(): TaxPortion = TaxPortion(Amount(BigDecimal.ONE), Currency.CALAMARI_FLAN)