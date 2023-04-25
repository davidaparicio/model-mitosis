package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import java.net.URI
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria as DomainCriteria
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey as DomainJourney
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journeys as DomainJourneys

@Resource
data class Criteria(val journeys: Journeys)

@Resource
data class Journey(val departureSpacePortId: URI, val departureSchedule: String, val arrivalSpacePortId: URI)

typealias Journeys = List<Journey>

fun DomainCriteria.toResource(): Criteria = Criteria(journeys.toResource())
private fun DomainJourneys.toResource(): List<Journey> = this.map { it.toResource() }
private fun DomainJourney.toResource(): Journey {
    val departure = departureSpacePortId
    val arrival = arrivalSpacePortId
    return Journey(URI(departure), departureSchedule.toString(), URI(arrival))
}