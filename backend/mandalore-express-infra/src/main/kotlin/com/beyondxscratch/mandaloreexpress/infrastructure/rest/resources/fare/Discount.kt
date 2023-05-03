package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Discount as DomainDiscount
import java.math.BigDecimal

@Resource
data class Discount(val amount : BigDecimal, val currency: Currency)

fun DomainDiscount.toResource() = Discount(this.amount.value, this.currency)
