package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price as DomainPrice
import java.math.BigDecimal

@Resource
data class Price(val amount : BigDecimal, val currency: Currency)

fun DomainPrice.toResource() = Price(this.amount.value, this.currency)
