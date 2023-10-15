package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency
import java.math.BigDecimal
import java.math.RoundingMode

data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }
}