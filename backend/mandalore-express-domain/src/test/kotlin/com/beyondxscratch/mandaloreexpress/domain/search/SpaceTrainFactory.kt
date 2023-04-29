package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.spacePort
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.Companion.fromJourneyIndex
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.firstClassFare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.randomFare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.schedule
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.secondClassFare
import kotlin.random.Random.Default.nextLong


fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        bound = OUTBOUND,
        originId = spacePort(CORUSCANT).id,
        destinationId = spacePort(MANDALORE).id,
        schedule = schedule(),
        fares = setOf(fare()))

fun outboundSpaceTrain(): SpaceTrain = spaceTrain()
fun inboundSpaceTrain(): SpaceTrain = SpaceTrain(
        number = "6235",
        bound = INBOUND,
        originId = spacePort(MANDALORE).id,
        destinationId = spacePort(CORUSCANT).id,
        schedule = schedule(),
        fares = setOf(fare()))

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                number = nextLong(1, 1000).toString(),
                fares = setOf(randomFare(), randomFare()),
                compatibleSpaceTrains = setOf(nextLong(1001, 2000).toString())
        )

fun spaceTrainsFrom(criteria: Criteria): SpaceTrains {
    return criteria.journeys
            .mapIndexed { journeyIndex, journey ->
                (1..2).map {
                    SpaceTrain(number = "${Planet.values()[journeyIndex]}$it",
                            bound = fromJourneyIndex(journeyIndex),
                            originId = journey.departureSpacePortId,
                            destinationId = journey.arrivalSpacePortId,
                            schedule = schedule(),
                            fares = setOf(firstClassFare(), secondClassFare())
                    )
                }
            }
            .flatten()
            .generateCompatibleSpaceTrainsFrom(criteria)
}

private fun List<SpaceTrain>.generateCompatibleSpaceTrainsFrom(criteria: Criteria): List<SpaceTrain> {
    if (criteria.journeys.size == 1) {
        return this
    }

    val spaceTrainByBound = groupBy { it.bound }
    val outbounds = spaceTrainByBound.getValue(OUTBOUND)
    val inbounds = spaceTrainByBound.getValue(INBOUND)

    return outbounds.mapIndexed { index, spaceTrain -> spaceTrain.copy(compatibleSpaceTrains = setOf(inbounds[index].number)) } + inbounds.mapIndexed { index, spaceTrain -> spaceTrain.copy(compatibleSpaceTrains = setOf(outbounds[index].number)) }
}
