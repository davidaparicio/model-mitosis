package com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.fare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.firstClassFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.randomFare
import java.time.LocalDateTime
import kotlin.random.Random
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

inline fun <reified T> schedule() : T = T::class.constructors.first().call(departure, departure.plusDays(7))
inline fun <reified T> randomSchedule() : T = T::class.constructors.first().call(departure, departure.plusDays(
    Random.nextLong(8, 20)))
fun SpaceTrain.withFirstClass(): SpaceTrain {
    return this.copy(fare = firstClassFare())
}

val departure = LocalDateTime.now()
    .plusDays(1)
    .withHour(10)
    .withMinute(0)
    .withSecond(0)
    .withNano(0)

fun SpaceTrain.numbered(number: String): SpaceTrain {
    return this.copy(number = number)
}

fun SpaceTrain.priced(price: Price): SpaceTrain {
    return this.copy(fare = fare.copy(price = price))
}

fun SpaceTrain.departing(date: LocalDateTime): SpaceTrain {
    return this.copy(schedule = this.schedule.copy(departure = date, arrival = date.plusDays(7)))
}