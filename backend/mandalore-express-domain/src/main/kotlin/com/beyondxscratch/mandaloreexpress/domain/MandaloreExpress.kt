package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.api.DomainService
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journeys
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Schedule
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import com.beyondxscratch.mandaloreexpress.domain.spi.SpacePorts
import java.time.LocalDateTime

@DomainService
class MandaloreExpress(
    override val spacePorts: SpacePorts,
    override val searches: Searches,
) :
    RetrieveSpacePorts,
    SearchForSpaceTrains {
    override fun `identified by`(id: String): SpacePort {
        return spacePorts.getAllSpacePorts().first { it.id == id }
    }

    override fun `having in their name`(partialName: String): Set<SpacePort> {
        return spacePorts.getAllSpacePorts()
            .filter { it `has a name containing` partialName }
            .toSet()
    }

    override fun satisfying(criteria: Criteria): Search {

        val (journeys) = criteria
        require(journeys.mustNotStayOnTheSamePlanet()) {
            "Cannot perform a trip departing and arriving on the same Planet"
        }

        val spaceTrains = generateSpaceTrains(journeys)
        return searches.save(Search(criteria = criteria, spaceTrains = spaceTrains))
    }

    private fun Journeys.mustNotStayOnTheSamePlanet() =
        none { spacePorts.find(it.departureSpacePortId).location == spacePorts.find(it.arrivalSpacePortId).location }

    private fun generateSpaceTrains(journeys: Journeys): SpaceTrains {
        return journeys.mapIndexed { journeyIndex, journey ->
            val bound = Bound.fromJourneyIndex(journeyIndex)
            val firstDepartureDeltaInMinutes = (0L..60L).random()
            (1..5).map { spaceTrainIndex ->
                generateSpaceTrain(journey, spaceTrainIndex, firstDepartureDeltaInMinutes, bound)
            }
        }.flatten()
    }

    private fun generateSpaceTrain(
        journey: Journey,
        spaceTrainIndex: Int,
        firstDepartureDeltaInMinutes: Long,
        bound: Bound
    ): SpaceTrain {
        val departure =
            computeDepartureSchedule(
                journey.departureSchedule,
                spaceTrainIndex,
                firstDepartureDeltaInMinutes
            )
        val arrivalSpacePort = spacePorts.find(journey.arrivalSpacePortId)
        return SpaceTrain(
            number = generateSpaceTrainNumber(
                arrivalSpacePort.location,
                spaceTrainIndex
            ),
            bound = bound,
            schedule = Schedule(
                departure,
                computeArrival(departure, spaceTrainIndex.toLong())
            ),
            destinationId = journey.arrivalSpacePortId,
            originId = journey.departureSpacePortId
        )
    }


}

private fun computeDepartureSchedule(
    criteriaDepartureSchedule: LocalDateTime,
    spaceTrainIndex: Int,
    firstDepartureDeltaInMinutes: Long
) =
    criteriaDepartureSchedule
        .plusMinutes(firstDepartureDeltaInMinutes)
        .plusHours(2L * (spaceTrainIndex - 1))

private fun generateSpaceTrainNumber(arrivalLocation: Planet, spaceTrainIndex: Int) =
    "${arrivalLocation.name.substring(0, 5)}$spaceTrainIndex${(10..99).random()}"

private fun computeArrival(departureSchedule: LocalDateTime, spaceTrainIndex: Long) =
    departureSchedule
        .plusHours(97 + spaceTrainIndex)
        .plusMinutes((20L..840L).random())

private fun SpacePorts.find(spacePortId: String): SpacePort {
    val id = if (spacePortId.contains("/")) spacePortId.splitToSequence("/").last() else spacePortId
    return getAllSpacePorts().first { it.id == id }
}