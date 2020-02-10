package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
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
        bound = OUTBOUND,
        origin = spacePort(EARTH),
        destination = spacePort(MOON),
        departureSchedule = departureSchedule,
        arrivalSchedule = departureSchedule.plusWeeks(1),
        fares = setOf(fare()))

fun outboundSpaceTrain(): SpaceTrain = spaceTrain()

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                number = nextLong(1, 1000).toString(),
                fares = setOf(randomFare(), randomFare()))

fun spaceTrainsFrom(criteria: Criteria): SpaceTrains = criteria.journeys
        .mapIndexed { index, journey ->
            SpaceTrain(number = index.toString(),
                    bound = Bound.fromJourneyIndex(index),
                    origin = journey.departureSpacePort,
                    destination = journey.arrivalSpacePort,
                    departureSchedule = journey.departureSchedule,
                    arrivalSchedule = journey.departureSchedule.plusDays(7),
                    fares = setOf(firstClassFare(), secondClassFare())
            )
        }