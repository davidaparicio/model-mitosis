package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import java.math.BigDecimal
import java.util.Currency

data class Price(val amount: BigDecimal, val currency: Currency) {
    init {
        require(amount > BigDecimal.ZERO) {
            "Price cannot be negative"
        }
    }

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }
}