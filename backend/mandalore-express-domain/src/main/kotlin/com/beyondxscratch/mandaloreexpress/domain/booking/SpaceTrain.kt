package com.beyondxscratch.mandaloreexpress.domain.booking

data class SpaceTrain(
    val number: String,
    val bound: Bound,
    val originId: String,
    val destinationId: String,
    val schedule: Schedule,
    val fare: Fare,
)