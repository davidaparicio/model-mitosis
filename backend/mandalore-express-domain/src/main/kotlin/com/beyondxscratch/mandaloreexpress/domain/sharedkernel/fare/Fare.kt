package com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare

import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
){
   // val taxPortion : TaxPortion get() = price.getTaxPortionOf(comfortClass.taxRate)
}
typealias Fares = Set<Fare>