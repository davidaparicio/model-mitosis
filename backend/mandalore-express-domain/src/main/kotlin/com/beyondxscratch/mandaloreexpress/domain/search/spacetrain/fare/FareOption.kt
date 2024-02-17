package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import java.util.*

data class FareOption(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)
typealias FareOptions = Set<FareOption>