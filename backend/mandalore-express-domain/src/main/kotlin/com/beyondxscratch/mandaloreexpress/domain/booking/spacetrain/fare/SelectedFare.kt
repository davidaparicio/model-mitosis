package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import java.util.UUID


data class SelectedFare(
    val id: UUID = UUID.randomUUID(),
    val comfortClass: ComfortClass,
    val price: Price,
)