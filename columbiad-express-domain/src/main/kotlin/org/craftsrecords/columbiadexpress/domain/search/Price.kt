package org.craftsrecords.columbiadexpress.domain.search

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.Currency

data class Price(val amount : BigDecimal, val currency: Currency){
    init {
        require(amount > ZERO){
            "Price cannot be negative"
        }
    }
}
