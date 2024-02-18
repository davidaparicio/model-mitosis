package com.beyondxscratch.mandaloreexpress.domain.sharedkernel

import java.math.BigDecimal
import java.math.RoundingMode

data class Amount(val value: BigDecimal) {
    init {
        require(value > BigDecimal.ZERO) {
            "Amount must be strictly positive"
        }
    }

    operator fun plus(amount: Amount): Amount = Amount(value + amount.value)

    operator fun minus(amount: Amount): Amount = Amount(value - amount.value)

    operator fun compareTo(amount: Amount): Int = value.compareTo(amount.value)
}

private operator fun BigDecimal.times(value: BigDecimal) : BigDecimal =
    this.multiply(value).setScale(this.scale(), RoundingMode.HALF_UP)