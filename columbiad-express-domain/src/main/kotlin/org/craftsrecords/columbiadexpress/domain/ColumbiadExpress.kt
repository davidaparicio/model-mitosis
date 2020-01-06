package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.search.Bound
import org.craftsrecords.columbiadexpress.domain.search.ComfortClass.FIRST
import org.craftsrecords.columbiadexpress.domain.search.ComfortClass.SECOND
import org.craftsrecords.columbiadexpress.domain.search.Fare
import org.craftsrecords.columbiadexpress.domain.search.Price
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrains
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journeys
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.api.DomainService
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.Searches
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.Locale.FRANCE

@DomainService
class ColumbiadExpress(override val spacePorts: SpacePorts, override val searches: Searches) : RetrieveSpacePorts, SearchForSpaceTrains {
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
        return journeys.mapIndexed { journeyIndex, journey ->
            val bound = Bound.fromJourneyIndex(journeyIndex)
            val firstDepartureDeltaInMinutes = (0L..60L).random()
            (1..3).map { spaceTrainIndex ->
                generateSpaceTrain(journey, spaceTrainIndex, firstDepartureDeltaInMinutes, bound)
            }
        }.flatten()
    }

    private fun generateSpaceTrain(journey: Journey, spaceTrainIndex: Int, firstDepartureDeltaInMinutes: Long, bound: Bound): SpaceTrain {
        val departureSchedule = computeDepartureSchedule(journey.departureSchedule, spaceTrainIndex, firstDepartureDeltaInMinutes)
        return SpaceTrain(number = generateSpaceTrainNumber(journey.arrivalSpacePort.location),
                bound = bound,
                departureSchedule = departureSchedule,
                arrivalSchedule = computeArrivalSchedule(departureSchedule, spaceTrainIndex.toLong()),
                destination = journey.arrivalSpacePort,
                origin = journey.departureSpacePort,
                fares = generateFares())
    }
}

private fun computeDepartureSchedule(criteriaDepartureSchedule: LocalDateTime, spaceTrainIndex: Int, firstDepartureDeltaInMinutes: Long) =
        criteriaDepartureSchedule
                .plusMinutes(firstDepartureDeltaInMinutes)
                .plusHours(2L * (spaceTrainIndex - 1))

private fun generateSpaceTrainNumber(arrivalLocation: AstronomicalBody) =
        "${arrivalLocation}${(100..200).random()}"

private fun computeArrivalSchedule(departureSchedule: LocalDateTime, spaceTrainIndex: Long) =
        departureSchedule
                .plusHours(97 * spaceTrainIndex)
                .plusMinutes((20L..840L).random())

private fun generateFares(): Set<Fare> {
    val currency = Currency.getInstance(FRANCE)
    return setOf(Fare(FIRST, Price(BigDecimal((180..400).random()), currency)),
            Fare(SECOND, Price(BigDecimal((150..200).random()), currency)))
}