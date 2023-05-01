package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price //TODO: Add Discount and make a test of basePrice - discount
) //TODO: Add Amenities

typealias Fares = Set<Fare>