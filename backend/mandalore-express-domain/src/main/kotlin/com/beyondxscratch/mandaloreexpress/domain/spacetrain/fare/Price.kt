package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.CALAMARI_FLAN
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import java.math.BigDecimal

data class Price(val amount: BigDecimal, val currency: Currency) {
    init {
        require(amount > BigDecimal.ZERO) {
            "Price must be strictly positive"
        }
        require(currency in arrayOf(REPUBLIC_CREDIT, CALAMARI_FLAN)) {
            "Republic credits will do fine."
        }
    }

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }
}