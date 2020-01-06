package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.springframework.hateoas.IanaLinkRelations
import java.net.URI
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria as DomainCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey as DomainJourney
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journeys as DomainJourneys

@Resource
data class Criteria(val journeys: Journeys)

@Resource
data class Journey(val departureSpacePort: URI, val departureSchedule: String, val arrivalSpacePort: URI)

typealias Journeys = List<Journey>

fun DomainCriteria.toResource(): Criteria = Criteria(journeys.toResource())
private fun DomainJourneys.toResource(): List<Journey> = this.map { it.toResource() }
private fun DomainJourney.toResource(): Journey {
    val departure = departureSpacePort.toResource()
    val arrival = arrivalSpacePort.toResource()
    return Journey(departure.getSelfLink(), departureSchedule.toString(), arrival.getSelfLink())
}

private fun SpacePort.getSelfLink(): URI = this.getLink(IanaLinkRelations.SELF).get().toUri()