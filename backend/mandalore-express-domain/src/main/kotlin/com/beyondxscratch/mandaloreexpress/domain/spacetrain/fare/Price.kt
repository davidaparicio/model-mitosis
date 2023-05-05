package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare


data class Price(val amount: Amount, val currency: Currency) {

    operator fun plus(anotherPrice: Price): Price {
        require(currency == anotherPrice.currency) {
            "Cannot sum prices with different currencies"
        }
        return Price(amount + anotherPrice.amount, currency)
    }

    fun apply(discount: Discount?): Price {
        return discount?.let {
            require(currency == discount.currency) {
                "Cannot sum prices with different currencies"
            }
            Price(amount - discount.amount, currency)
        } ?: copy()
    }
}