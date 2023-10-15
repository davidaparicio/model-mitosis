package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxRate

enum class ComfortClass(val taxRate: TaxRate) {
    FIRST(TaxRate("0.2".toBigDecimal())),
    SECOND(TaxRate("0.1".toBigDecimal())),
}