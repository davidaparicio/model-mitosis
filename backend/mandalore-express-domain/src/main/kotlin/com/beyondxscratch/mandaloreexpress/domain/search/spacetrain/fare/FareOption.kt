package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import java.util.UUID

data class FareOption(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)
typealias FareOptions = Set<FareOption>