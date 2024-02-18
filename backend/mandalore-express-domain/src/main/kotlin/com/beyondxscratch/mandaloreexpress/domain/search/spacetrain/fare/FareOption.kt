package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Price
import java.util.UUID

data class FareOption(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)
typealias FareOptions = Set<FareOption>