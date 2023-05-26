package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxRate
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency
import java.math.BigDecimal


data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }

    fun getTaxPortionOf(taxRate: TaxRate): TaxPortion {
        val taxRatio = BigDecimal.ONE - BigDecimal.ONE / (BigDecimal.ONE + taxRate.value)
        return TaxPortion(amount * taxRatio, currency)
    }
}