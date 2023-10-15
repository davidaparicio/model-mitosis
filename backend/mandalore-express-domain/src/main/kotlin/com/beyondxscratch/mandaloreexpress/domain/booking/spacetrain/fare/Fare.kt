package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
){
   val taxPortion : TaxPortion get() = price.getTaxPortionOf(comfortClass.taxRate)
}