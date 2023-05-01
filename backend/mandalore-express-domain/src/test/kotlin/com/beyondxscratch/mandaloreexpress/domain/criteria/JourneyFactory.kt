package com.beyondxscratch.mandaloreexpress.domain.criteria

import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.values
import com.beyondxscratch.mandaloreexpress.domain.spaceport.spacePort
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.LocalTime
import kotlin.random.Random

fun journey(): Journey = Journey(
        spacePort(CORUSCANT).id,
        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)),
        spacePort(MANDALORE).id)

fun outboundJourney(): Journey = journey()
fun inboundJourney(): Journey = inboundOf(outboundJourney())

fun randomJourney(): Journey {
    val numberOfDays = Random.nextLong(1, 30)
    val departurePlanet = values().random()

    return Journey(
            spacePort(departurePlanet).id,
            now().plusDays(numberOfDays),
            spacePort(values().asIterable().minus(departurePlanet).random()).id)
}

fun inboundOf(journey: Journey): Journey =
        journey.copy(
                departureSpacePortId = journey.arrivalSpacePortId,
                arrivalSpacePortId = journey.departureSpacePortId,
                departureSchedule = journey.departureSchedule.plusDays(5)
        )

infix fun Journey.departingAt(departureSchedule: LocalDateTime) = this.copy(departureSchedule = departureSchedule)