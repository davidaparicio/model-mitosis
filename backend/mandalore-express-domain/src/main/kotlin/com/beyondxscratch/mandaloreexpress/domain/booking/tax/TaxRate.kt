package com.beyondxscratch.mandaloreexpress.domain.booking.tax

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class TaxRate(val value: BigDecimal){
    init {
        require(value > ZERO){
            "TaxRate must be strictly positive"
        }
    }
}