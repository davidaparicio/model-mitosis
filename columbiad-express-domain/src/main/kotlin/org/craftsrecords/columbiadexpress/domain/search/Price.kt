package org.craftsrecords.columbiadexpress.domain.search

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.*

data class Price(val amount: BigDecimal, val currency: Currency) {
    init {
        require(amount > ZERO) {
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
