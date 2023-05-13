package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.firstClassSelectedFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.randomSelectedFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.secondClassSelectedFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.selectedFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.randomSchedule
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.schedule
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

fun spaceTrain(): SpaceTrain = SpaceTrain(
    number = "6127",
    originId = "CORUSCANT",
    destinationId = "MANDALORE",
    schedule = schedule(),
    selectedFare = selectedFare()
)

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
    .copy(
        number = nextLong(1, 1000).toString(),
        schedule = randomSchedule(),
        selectedFare = randomSelectedFare()
    )

fun SpaceTrain.withFirstClass(): SpaceTrain {
    return this.copy(selectedFare = firstClassSelectedFare())
}

fun SpaceTrain.withSecondClass(): SpaceTrain {
    return this.copy(selectedFare = secondClassSelectedFare())
}

fun SpaceTrain.numbered(number: String): SpaceTrain {
    return this.copy(number = number)
}

fun SpaceTrain.priced(price: Price): SpaceTrain {
    return this.copy(selectedFare = selectedFare.copy(price = price))
}

fun SpaceTrain.departing(date: LocalDateTime): SpaceTrain {
    return this.copy(schedule = this.schedule.copy(departure = date, arrival = date.plusDays(7)))
}