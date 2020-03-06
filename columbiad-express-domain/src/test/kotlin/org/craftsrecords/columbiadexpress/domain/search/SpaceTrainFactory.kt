package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.Companion.fromJourneyIndex
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.INBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.firstClassFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.randomFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.schedule
import org.craftsrecords.columbiadexpress.domain.sharedkernel.secondClassFare
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import kotlin.random.Random.Default.nextLong


fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        bound = OUTBOUND,
        originId = spacePort(EARTH).id,
        destinationId = spacePort(MOON).id,
        schedule = schedule(),
        fares = setOf(fare()))

fun outboundSpaceTrain(): SpaceTrain = spaceTrain()
fun inboundSpaceTrain(): SpaceTrain = SpaceTrain(
        number = "6235",
        bound = INBOUND,
        originId = spacePort(MOON).id,
        destinationId = spacePort(EARTH).id,
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
                    SpaceTrain(number = "${AstronomicalBody.values()[journeyIndex]}$it",
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
