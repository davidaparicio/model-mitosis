package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxRate
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency
import java.math.BigDecimal
import java.math.BigDecimal.*
import java.math.RoundingMode


data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }

    fun getTaxPortionOf(taxRate: TaxRate): TaxPortion {
        val ratio = ONE - ONE / (ONE + taxRate.value)
        return TaxPortion(amount * ratio, currency)
    }

}
operator fun BigDecimal.div(value: BigDecimal) : BigDecimal = this.divide(value, 50, RoundingMode.HALF_UP)