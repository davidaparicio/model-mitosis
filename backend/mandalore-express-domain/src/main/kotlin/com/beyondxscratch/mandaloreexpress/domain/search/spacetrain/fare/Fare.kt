package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import java.util.*

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)
typealias Fares = Set<Fare>