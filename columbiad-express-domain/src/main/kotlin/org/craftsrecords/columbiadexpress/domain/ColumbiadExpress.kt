package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.api.BookSomeSpaceTrains
import org.craftsrecords.columbiadexpress.domain.api.DomainService
import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.domain.api.SelectSpaceTrain
import org.craftsrecords.columbiadexpress.domain.booking.Booking
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrains
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journeys
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.INBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.ComfortClass.FIRST
import org.craftsrecords.columbiadexpress.domain.sharedkernel.ComfortClass.SECOND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Price
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Schedule
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spi.Bookings
import org.craftsrecords.columbiadexpress.domain.spi.Searches
import org.craftsrecords.columbiadexpress.domain.spi.SpacePorts
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.Locale.FRANCE
import java.util.UUID
import org.craftsrecords.columbiadexpress.domain.booking.SpaceTrain as BookingSpaceTrain

@DomainService
class ColumbiadExpress(override val spacePorts: SpacePorts, override val searches: Searches, override val bookings: Bookings) :
        RetrieveSpacePorts,
        SearchForSpaceTrains,
        SelectSpaceTrain,
        BookSomeSpaceTrains {
    override fun `identified by`(id: String): SpacePort {
        return spacePorts.getAllSpacePorts().first { it.id == id }
    }

    override fun `having in their name`(partialName: String): Set<SpacePort> {
        return spacePorts.getAllSpacePorts()
                .filter { it `has a name containing` partialName }
                .toSet()
    }

    override fun satisfying(criteria: Criteria): Search {
        val spaceTrains = generateSpaceTrains(criteria.journeys)
        return searches.save(Search(criteria = criteria, spaceTrains = spaceTrains))
    }

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
            inbound.copy(compatibleSpaceTrains = outboundsWithCompatibilities
                    .filter { outbound ->
                        outbound.compatibleSpaceTrains.contains(inbound.number)
                    }.map(SpaceTrain::number)
                    .toSet()
            )
        }.filter { it.compatibleSpaceTrains.isNotEmpty() }

        return outboundsWithCompatibilities + inboundsWithCompatibilities
    }

    private fun generateSpaceTrain(journey: Journey, spaceTrainIndex: Int, firstDepartureDeltaInMinutes: Long, bound: Bound): SpaceTrain {
        val departure = computeDepartureSchedule(journey.departureSchedule, spaceTrainIndex, firstDepartureDeltaInMinutes)
        return SpaceTrain(number = generateSpaceTrainNumber(journey.arrivalSpacePort.location, spaceTrainIndex),
                bound = bound,
                schedule = Schedule(departure, computeArrival(departure, spaceTrainIndex.toLong())),
                destination = journey.arrivalSpacePort,
                origin = journey.departureSpacePort,
                fares = generateFares())
    }

    override fun selectFareOfSpaceTrainInSearch(spaceTrainNumber: String, fareId: UUID, searchId: UUID, resetSelection: Boolean): Search {
        val search = searches `find search identified by` searchId
                ?: throw NoSuchElementException("unknown search id $searchId")
        val searchWithSelection = search.selectSpaceTrainWithFare(spaceTrainNumber, fareId, resetSelection)
        return searches.save(searchWithSelection)
    }

    override fun `from the selection of`(search: Search): Booking {
        when {

            !search.isSelectionComplete() -> {
                throw CannotBookAPartialSelection()
            }

            else -> {
                val selection = search.selection
                val spaceTrains =
                        selection
                                .spaceTrainsByBound
                                .sortedBy { it.key.ordinal }
                                .map { it.value }
                                .map { selectedSpaceTrain ->
                                    val spaceTrain = search.spaceTrains.first { it.number == selectedSpaceTrain.spaceTrainNumber }
                                    val fare = spaceTrain.fares.first { it.id == selectedSpaceTrain.fareId }
                                    BookingSpaceTrain(spaceTrain.number, spaceTrain.origin, spaceTrain.destination, spaceTrain.schedule, fare)
                                }
                return bookings.save(Booking(spaceTrains = spaceTrains))
            }
        }

    }
}

private fun computeDepartureSchedule(criteriaDepartureSchedule: LocalDateTime, spaceTrainIndex: Int, firstDepartureDeltaInMinutes: Long) =
        criteriaDepartureSchedule
                .plusMinutes(firstDepartureDeltaInMinutes)
                .plusHours(2L * (spaceTrainIndex - 1))

private fun generateSpaceTrainNumber(arrivalLocation: AstronomicalBody, spaceTrainIndex: Int) =
        "${arrivalLocation}$spaceTrainIndex${(10..99).random()}"

private fun computeArrival(departureSchedule: LocalDateTime, spaceTrainIndex: Long) =
        departureSchedule
                .plusHours(97 * spaceTrainIndex)
                .plusMinutes((20L..840L).random())

private fun generateFares(): Set<Fare> {
    val currency = Currency.getInstance(FRANCE)
    return setOf(Fare(comfortClass = FIRST, price = Price(BigDecimal((180..400).random()), currency)),
            Fare(comfortClass = SECOND, price = Price(BigDecimal((150..200).random()), currency)))
}