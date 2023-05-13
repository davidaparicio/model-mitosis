package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Fare

data class SpaceTrain(
    val number: String,
    val originId: String,
    val destinationId: String,
    val schedule: Schedule,
    val fare: Fare,
)