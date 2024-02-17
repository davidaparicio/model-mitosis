package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import java.util.*

data class SelectedFare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)