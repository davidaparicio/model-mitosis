package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.search.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.search.api.SelectSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journeys
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Searches
import com.beyondxscratch.mandaloreexpress.domain.search.spi.SpacePorts
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Schedule
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Amount
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.FareOption
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.Price
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@DomainService
class SpaceTrainsFinder(
    override val spacePorts: SpacePorts,
    override val searches: Searches,
) :
    RetrieveSpacePorts,
    SearchForSpaceTrains,
    SelectSpaceTrain {
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

        val spaceTrains = findMatchingSpaceTrainsFor(journeys)
        return searches.save(Search(criteria = criteria, spaceTrains = spaceTrains))
    }

    override fun selectFareOfSpaceTrainInSearch(
        spaceTrainNumber: String,
        fareId: UUID,
        searchId: UUID,
        resetSelection: Boolean
    ): Search {
        val search = searches `find search identified by` searchId
            ?: throw NoSuchElementException("unknown search id $searchId")
        val searchWithSelection = search.selectSpaceTrainWithFare(spaceTrainNumber, fareId, resetSelection)
        return searches.save(searchWithSelection)
    }

    private fun Journeys.mustNotStayOnTheSamePlanet() =
        none { spacePorts.find(it.departureSpacePortId).location == spacePorts.find(it.arrivalSpacePortId).location }

    private fun findMatchingSpaceTrainsFor(journeys: Journeys): SpaceTrains {
        val spaceTrains = journeys.mapIndexed { journeyIndex, journey ->
            val bound = Bound.fromJourneyIndex(journeyIndex)
            val firstDepartureDeltaInMinutes = (0L..60L).random()
            (1..5).map { spaceTrainIndex ->
                getSpaceTrain(journey, spaceTrainIndex, firstDepartureDeltaInMinutes, bound)
            }
        }.flatten()

        return spaceTrains.computeCompatibilities()
    }

    private fun List<SpaceTrain>.computeCompatibilities(): List<SpaceTrain> {
        val inbounds = filter { it.bound == INBOUND }
        if (inbounds.isEmpty()) {
            return this
        }

        val outbounds = filter { it.bound == OUTBOUND }
        val inboundNumbers = inbounds.map { it.number }

        val outboundsWithCompatibilities =
            outbounds.map {
                it.copy(compatibleSpaceTrains = setOf(inboundNumbers.random(), inboundNumbers.random()))
            }

        val inboundsWithCompatibilities = inbounds.map { inbound ->
            inbound.copy(
                compatibleSpaceTrains = outboundsWithCompatibilities
                    .filter { outbound ->
                        outbound.compatibleSpaceTrains.contains(inbound.number)
                    }.map(SpaceTrain::number)
                    .toSet()
            )
        }.filter { it.compatibleSpaceTrains.isNotEmpty() }

        return outboundsWithCompatibilities + inboundsWithCompatibilities
    }

    private fun getSpaceTrain(
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
            number = computeSpaceTrainNumber(
                arrivalSpacePort.location,
                spaceTrainIndex
            ),
            bound = bound,
            schedule = Schedule(
                departure,
                computeArrival(departure, spaceTrainIndex.toLong())
            ),
            destinationId = journey.arrivalSpacePortId,
            originId = journey.departureSpacePortId,
            fareOptions = computeFares()
        )
    }


    private fun computeDepartureSchedule(
        criteriaDepartureSchedule: LocalDateTime,
        spaceTrainIndex: Int,
        firstDepartureDeltaInMinutes: Long
    ) =
        criteriaDepartureSchedule
            .plusMinutes(firstDepartureDeltaInMinutes)
            .plusHours(2L * (spaceTrainIndex - 1))

    private fun computeSpaceTrainNumber(arrivalLocation: Planet, spaceTrainIndex: Int) =
        "${arrivalLocation.name.substring(0, 5)}$spaceTrainIndex${(10..99).random()}"

    private fun computeArrival(departureSchedule: LocalDateTime, spaceTrainIndex: Long) =
        departureSchedule
            .plusHours(97 + spaceTrainIndex)
            .plusMinutes((20L..840L).random())

    private fun computeFares(): Set<FareOption> {


        return setOf(
            computeFare(FIRST),
            computeFare(SECOND)
        )
    }

    private fun computeFare(comfortClass: ComfortClass): FareOption {

        val amountRange = when (comfortClass) {
            FIRST -> 180..400
            else -> 150..200
        }

        val price = Price(Amount(BigDecimal(amountRange.random())), REPUBLIC_CREDIT)
        return FareOption(
            comfortClass = comfortClass,
            price = price,
        )
    }

    private fun SpacePorts.find(spacePortId: String): SpacePort {
        val id = if (spacePortId.contains("/")) spacePortId.splitToSequence("/").last() else spacePortId
        return getAllSpacePorts().first { it.id == id }
    }
}
