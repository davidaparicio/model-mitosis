package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.fare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.firstClassFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.randomFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

fun spaceTrain(): SpaceTrain = SpaceTrain(
    number = "6127",
    originId = "CORUSCANT",
    destinationId = "MANDALORE",
    schedule = schedule(),
    fare = fare()
)

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
    .copy(
        number = nextLong(1, 1000).toString(),
        schedule = randomSchedule(),
        fare = randomFare()
    )

fun SpaceTrain.withFirstClass(): SpaceTrain {
    return this.copy(fare = firstClassFare())
}

fun SpaceTrain.numbered(number: String): SpaceTrain {
    return this.copy(number = number)
}

fun SpaceTrain.priced(price: Price): SpaceTrain {
    return this.copy(fare = fare.copy(price = price))
}

fun SpaceTrain.departing(date: LocalDateTime): SpaceTrain {
    return this.copy(schedule = this.schedule.copy(departure = date, arrival = date.plusDays(7)))
}