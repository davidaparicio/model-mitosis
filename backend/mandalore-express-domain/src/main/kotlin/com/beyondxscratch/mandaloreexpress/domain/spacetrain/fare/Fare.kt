package com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare

import java.util.UUID

data class Fare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val basePrice: Price,
    val discount: Discount? = null
    //TODO: Add Amenities
) {

    val price: Price get() = discount?.let { basePrice.apply(it) } ?: basePrice

    init {
        discount?.let {
            require(basePrice.amount >= it.amount) {
                "Cannot have a discount higher than the base price"
            }
        }
    }
}

typealias Fares = Set<Fare>