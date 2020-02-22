package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.firstClassFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.randomFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.schedule
import org.craftsrecords.columbiadexpress.domain.sharedkernel.secondClassFare
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import kotlin.random.Random.Default.nextLong


fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        bound = OUTBOUND,
        origin = spacePort(EARTH),
        destination = spacePort(MOON),
        schedule = schedule(),
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
                    schedule = schedule(),
                    fares = setOf(firstClassFare(), secondClassFare())
            )
        }