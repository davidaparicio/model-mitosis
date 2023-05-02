package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import java.math.BigDecimal

data class Amount(val value: BigDecimal){
    init {
        require(value > BigDecimal.ZERO) {
            "Amount must be strictly positive"
        }
    }

    operator fun plus(amount: Amount) : Amount = Amount(value  + amount.value)

    operator fun minus(amount: Amount) : Amount = Amount(value - amount.value)
}
