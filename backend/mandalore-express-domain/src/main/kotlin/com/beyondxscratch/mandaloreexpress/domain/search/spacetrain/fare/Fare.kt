package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)
typealias Fares = Set<Fare>