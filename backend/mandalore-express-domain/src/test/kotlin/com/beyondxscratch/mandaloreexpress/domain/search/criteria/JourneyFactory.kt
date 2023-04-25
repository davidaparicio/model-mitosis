package com.beyondxscratch.mandaloreexpress.domain.search.criteria

import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody.EARTH
import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody.MOON
import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody.values
import com.beyondxscratch.mandaloreexpress.domain.spaceport.spacePort
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.LocalTime
import kotlin.random.Random

fun journey(): Journey = Journey(
        spacePort(EARTH).id,
        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)),
        spacePort(MOON).id)

fun outboundJourney(): Journey = journey()
fun inboundJourney(): Journey = inboundOf(outboundJourney())

fun randomJourney(): Journey {
    val numberOfDays = Random.nextLong(1, 30)
    val departureAstronomicalBody = values().random()

    return Journey(
            spacePort(departureAstronomicalBody).id,
            now().plusDays(numberOfDays),
            spacePort(values().asIterable().minus(departureAstronomicalBody).random()).id)
}

fun inboundOf(journey: Journey): Journey =
        journey.copy(
                departureSpacePortId = journey.arrivalSpacePortId,
                arrivalSpacePortId = journey.departureSpacePortId,
                departureSchedule = journey.departureSchedule.plusDays(5)
        )

infix fun Journey.departingAt(departureSchedule: LocalDateTime) = this.copy(departureSchedule = departureSchedule)