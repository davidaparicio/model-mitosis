package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.values
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.LocalTime
import kotlin.random.Random

fun journey(): Journey = Journey(
        spacePort(EARTH),
        LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0)),
        spacePort(MOON))

fun randomJourney(): Journey {
    val numberOfDays = Random.nextLong(1, 30)
    val departureAstronomicalBody = values().random()

    return Journey(
            spacePort(departureAstronomicalBody),
            now().plusDays(numberOfDays),
            spacePort(values().asIterable().minus(departureAstronomicalBody).random()))
}

fun connectionTo(journey: Journey): Journey = journey.copy(departureSpacePort = journey.arrivalSpacePort, arrivalSpacePort = journey.departureSpacePort)

infix fun Journey.departingAt(departureSchedule: LocalDateTime) = this.copy(departureSchedule = departureSchedule)