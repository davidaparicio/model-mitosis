package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.Bound.Companion.fromJourneyIndex
import com.beyondxscratch.mandaloreexpress.domain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.spaceport.spacePort
import kotlin.random.Random.Default.nextLong


fun spaceTrain(): SpaceTrain = SpaceTrain(
    number = "6127",
    bound = OUTBOUND,
    originId = spacePort(CORUSCANT).id,
    destinationId = spacePort(MANDALORE).id,
    schedule = schedule()
)

fun outboundSpaceTrain(): SpaceTrain = spaceTrain()
fun inboundSpaceTrain(): SpaceTrain = SpaceTrain(
    number = "6235",
    bound = INBOUND,
    originId = spacePort(MANDALORE).id,
    destinationId = spacePort(CORUSCANT).id,
    schedule = schedule()
)

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
    .copy(
        number = nextLong(1, 1000).toString()
    )

fun spaceTrainsFrom(criteria: Criteria): SpaceTrains {
    return criteria.journeys
        .mapIndexed { journeyIndex, journey ->
            (1..2).map {
                SpaceTrain(
                    number = "${Planet.values()[journeyIndex]}$it",
                    bound = fromJourneyIndex(journeyIndex),
                    originId = journey.departureSpacePortId,
                    destinationId = journey.arrivalSpacePortId,
                    schedule = schedule(),
                )
            }
        }
        .flatten()
}
