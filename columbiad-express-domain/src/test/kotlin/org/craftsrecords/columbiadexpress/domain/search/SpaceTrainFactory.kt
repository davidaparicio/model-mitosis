package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import java.time.LocalDateTime.now
import java.util.UUID
import kotlin.random.Random.Default.nextLong

private val departureSchedule =
        now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

fun spaceTrain(): SpaceTrain = SpaceTrain(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426655440000"),
        number = "6127",
        bound = OUTBOUND,
        origin = spacePort(EARTH),
        destination = spacePort(MOON),
        departureSchedule = departureSchedule,
        arrivalSchedule = departureSchedule.plusWeeks(1),
        fares = setOf(fare()))

fun outboundSpaceTrain(): SpaceTrain = spaceTrain()

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                id = UUID.randomUUID(),
                number = nextLong(1, 1000).toString(),
                fares = setOf(randomFare(), randomFare()))