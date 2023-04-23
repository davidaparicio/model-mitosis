package com.beyondxscratch.columbiadexpress.domain

import com.beyondxscratch.columbiadexpress.domain.api.BookSpaceTrains
import com.beyondxscratch.columbiadexpress.domain.api.DomainService
import com.beyondxscratch.columbiadexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.columbiadexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.columbiadexpress.domain.api.SelectSpaceTrain
import com.beyondxscratch.columbiadexpress.domain.booking.Booking
import com.beyondxscratch.columbiadexpress.domain.search.Search
import com.beyondxscratch.columbiadexpress.domain.search.SpaceTrain
import com.beyondxscratch.columbiadexpress.domain.search.SpaceTrains
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Criteria
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Journey
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Journeys
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound.INBOUND
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.ComfortClass.FIRST
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.ComfortClass.SECOND
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Fare
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Price
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Schedule
import com.beyondxscratch.columbiadexpress.domain.spaceport.AstronomicalBody
import com.beyondxscratch.columbiadexpress.domain.spaceport.SpacePort
import com.beyondxscratch.columbiadexpress.domain.spi.Bookings
import com.beyondxscratch.columbiadexpress.domain.spi.Searches
import com.beyondxscratch.columbiadexpress.domain.spi.SpacePorts
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.Locale.FRANCE
import java.util.UUID
import com.beyondxscratch.columbiadexpress.domain.booking.SpaceTrain as BookingSpaceTrain

@DomainService
class ColumbiadExpress(
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
        require(journeys.mustNotStayOnTheSameAstronomicalBody()) {
            "Cannot perform a trip departing and arriving on the same AstronomicalBody"
        }

        val spaceTrains = generateSpaceTrains(journeys)
        return searches.save(Search(criteria = criteria, spaceTrains = spaceTrains))
    }

    private fun Journeys.mustNotStayOnTheSameAstronomicalBody() =
        none { spacePorts.find(it.departureSpacePortId).location == spacePorts.find(it.arrivalSpacePortId).location }

    private fun generateSpaceTrains(journeys: Journeys): SpaceTrains {
        val spaceTrains = journeys.mapIndexed { journeyIndex, journey ->
            val bound = Bound.fromJourneyIndex(journeyIndex)
            val firstDepartureDeltaInMinutes = (0L..60L).random()
            (1..5).map { spaceTrainIndex ->
                generateSpaceTrain(journey, spaceTrainIndex, firstDepartureDeltaInMinutes, bound)
            }
        }.flatten()

        return spaceTrains.generateCompatibilities()
    }

    private fun List<SpaceTrain>.generateCompatibilities(): List<SpaceTrain> {
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

    private fun generateSpaceTrain(
        journey: Journey,
        spaceTrainIndex: Int,
        firstDepartureDeltaInMinutes: Long,
        bound: Bound
    ): SpaceTrain {
        val departure =
            com.beyondxscratch.columbiadexpress.domain.computeDepartureSchedule(
                journey.departureSchedule,
                spaceTrainIndex,
                firstDepartureDeltaInMinutes
            )
        val arrivalSpacePort = spacePorts.find(journey.arrivalSpacePortId)
        return SpaceTrain(
            number = com.beyondxscratch.columbiadexpress.domain.generateSpaceTrainNumber(
                arrivalSpacePort.location,
                spaceTrainIndex
            ),
            bound = bound,
            schedule = Schedule(departure,
                com.beyondxscratch.columbiadexpress.domain.computeArrival(departure, spaceTrainIndex.toLong())
            ),
            destinationId = journey.arrivalSpacePortId,
            originId = journey.departureSpacePortId,
            fares = com.beyondxscratch.columbiadexpress.domain.generateFares()
        )
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
        when {

            !search.isSelectionComplete() -> {
                throw com.beyondxscratch.columbiadexpress.domain.CannotBookAPartialSelection()
            }

            else -> {
                val selection = search.selection
                val spaceTrains =
                    selection
                        .spaceTrainsByBound
                        .sortedBy { it.key.ordinal }
                        .map { it.value }
                        .map { selectedSpaceTrain ->
                            val spaceTrain = search.getSpaceTrainWithNumber(selectedSpaceTrain.spaceTrainNumber)
                            val fare = spaceTrain.fares.first { it.id == selectedSpaceTrain.fareId }
                            BookingSpaceTrain(
                                spaceTrain.number,
                                spaceTrain.originId,
                                spaceTrain.destinationId,
                                spaceTrain.schedule,
                                fare
                            )
                        }
                return bookings.save(Booking(spaceTrains = spaceTrains))
            }
        }

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

private fun generateSpaceTrainNumber(arrivalLocation: AstronomicalBody, spaceTrainIndex: Int) =
    "${arrivalLocation}$spaceTrainIndex${(10..99).random()}"

private fun computeArrival(departureSchedule: LocalDateTime, spaceTrainIndex: Long) =
    departureSchedule
        .plusHours(97 + spaceTrainIndex)
        .plusMinutes((20L..840L).random())

private fun generateFares(): Set<Fare> {
    val currency = Currency.getInstance(FRANCE)
    return setOf(
        Fare(comfortClass = FIRST, price = Price(BigDecimal((180..400).random()), currency)),
        Fare(comfortClass = SECOND, price = Price(BigDecimal((150..200).random()), currency))
    )
}

private fun SpacePorts.find(spacePortId: String): SpacePort {
    val id = if (spacePortId.contains("/")) spacePortId.splitToSequence("/").last() else spacePortId
    return getAllSpacePorts().first { it.id == id }
}