package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare


data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }
}