package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.Journey.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import java.time.LocalDateTime.now
import kotlin.random.Random.Default.nextLong

private val departureSchedule =
        now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        journey = OUTBOUND,
        origin = spacePort(EARTH),
        destination = spacePort(MOON),
        departureSchedule = departureSchedule,
        arrivalSchedule = departureSchedule.plusWeeks(1),
        fares = setOf(fare()))

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                number = nextLong(1, 1000).toString(),
                fares = setOf(randomFare(), randomFare()))