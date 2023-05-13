package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import java.time.Duration
import java.time.LocalDateTime

data class Schedule(val departure: LocalDateTime, val arrival: LocalDateTime) {
    val duration: Duration = Duration.between(departure, arrival)

    init {
        require(arrival.isAfter(departure)) {
            "arrival cannot precede the departure"
        }
    }
}