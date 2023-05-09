package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.SelectSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journeys
import com.beyondxscratch.mandaloreexpress.domain.exceptions.CannotBookAPartialSelection
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Schedule
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.FIRST
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass.SECOND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Discount
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Fare
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import com.beyondxscratch.mandaloreexpress.domain.spi.SpacePorts
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@DomainService
class MandaloreExpress(
    override val spacePorts: SpacePorts,
    override val searches: Searches,
    override val bookings: Bookings
) :
    RetrieveSpacePorts,
    SearchForSpaceTrains,
    SelectSpaceTrain,
    BookSpaceTrains {
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

    override fun `from the selection of`(search: Search): Booking {
        return when {
            !search.isSelectionComplete() -> throw CannotBookAPartialSelection()
            else -> {
                val spaceTrainsToBook = convertSelectedSpaceTrainsFrom(search)
                bookings.save(Booking(spaceTrains = spaceTrainsToBook))
            }
        }

    }

    private fun convertSelectedSpaceTrainsFrom(search: Search) =
        search.getSelectedSpaceTrainsSortedByBound()
            .map { selectedSpaceTrain ->
                val (spaceTrain, fareId) = selectedSpaceTrain
                val fare = spaceTrain.getFare(fareId)

                return@map SpaceTrain(
                    spaceTrain.number,
                    spaceTrain.bound,
                    spaceTrain.originId,
                    spaceTrain.destinationId,
                    spaceTrain.schedule,
                    setOf<Fare>(fare),
                    spaceTrain.compatibleSpaceTrains
                )
            }


    private fun Search.getSelectedSpaceTrainsSortedByBound() =
        this.selection.spaceTrainsByBound.sortedBy { it.key.ordinal }
            .map { Pair(this.getSpaceTrainWithNumber(it.value.spaceTrainNumber), it.value.fareId) }

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
            fares = computeFares()
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

    private fun computeFares(): Set<Fare> {


        return setOf(
            computeFare(FIRST),
            computeFare(SECOND)
        )
    }

    private fun computeFare(comfortClass: ComfortClass): Fare {

        val amountRange = when (comfortClass) {
            FIRST -> 180..400
            else -> 150..200
        }

        val basePrice = Price(Amount(BigDecimal(amountRange.random())), REPUBLIC_CREDIT)
        return Fare(
            comfortClass = comfortClass,
            basePrice = basePrice,
            discount = basePrice.generateDiscount(comfortClass)
        )
    }

    private fun Price.generateDiscount(comfortClass : ComfortClass): Discount? {
        val shouldHaveADiscount = comfortClass == FIRST
        return if (shouldHaveADiscount) {
            Discount(Amount(BigDecimal((1..this.amount.value.intValueExact() - 10).random())), this.currency)
        } else null
    }

    private fun SpacePorts.find(spacePortId: String): SpacePort {
        val id = if (spacePortId.contains("/")) spacePortId.splitToSequence("/").last() else spacePortId
        return getAllSpacePorts().first { it.id == id }
    }
}
