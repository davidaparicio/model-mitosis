package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare

import java.math.BigDecimal
import java.math.RoundingMode


data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }

    // fun getTaxPortionOf(taxRate: TaxRate): TaxPortion {
    //    val taxRatio = BigDecimal.ONE - BigDecimal.ONE / (BigDecimal.ONE + taxRate.value)
    //    return TaxPortion(amount * taxRatio, currency)
    //}
}

private operator fun BigDecimal.div(value: BigDecimal): BigDecimal = this.divide(value, 50, RoundingMode.HALF_UP)