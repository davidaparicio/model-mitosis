package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.sharedkernel

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import java.math.BigDecimal
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price as DomainPrice

@Resource
data class Price(val amount : BigDecimal, val currency: Currency)

fun DomainPrice.toResource() = Price(this.amount.value, this.currency)