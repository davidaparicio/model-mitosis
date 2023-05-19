package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import java.math.BigDecimal
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion as DomainTaxPortion

@Resource
data class TaxPortion(val amount : BigDecimal, val currency: Currency)

fun DomainTaxPortion.toResource() = TaxPortion(this.amount.value, this.currency)
