package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SelectedFare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Schedule

data class SpaceTrain(
    val number: String,
    val originId: String,
    val destinationId: String,
    val schedule: Schedule,
    val selectedFare: SelectedFare,
)