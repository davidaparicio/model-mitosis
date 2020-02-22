package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.oneWayCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.roundTripCriteria
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.INBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.OUTBOUND
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare
import java.util.UUID.fromString
import java.util.UUID.randomUUID

fun search(): Search {
    val criteria = oneWayCriteria()
    return Search(fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), criteria, spaceTrainsFrom(criteria))
}

fun randomSearch(): Search = search().copy(id = randomUUID())
fun oneWaySearch() = search()
fun roundTripSearch(): Search {
    val criteria = roundTripCriteria()
    return Search(id = fromString("b6c0bb24-8e3d-4375-9d34-d3a5fb8d6b4a"), criteria = criteria, spaceTrains = spaceTrainsFrom(criteria))
}

fun Search.selectAnOutboundSpaceTrain(): Triple<Search, SpaceTrain, Fare> {
    return doABoundSelectionFor(OUTBOUND)
}

fun Search.selectAnInboundSpaceTrain(): Triple<Search, SpaceTrain, Fare> {
    return doABoundSelectionFor(INBOUND)
}

fun Triple<Search, SpaceTrain, Fare>.selectAnInboundSpaceTrain(): Triple<Search, SpaceTrain, Fare> {
    return first.selectAnInboundSpaceTrain()
}

fun Triple<Search, SpaceTrain, Fare>.selectAnOutboundSpaceTrain(): Triple<Search, SpaceTrain, Fare> {
    return first.selectAnOutboundSpaceTrain()
}

private fun Search.doABoundSelectionFor(bound: Bound): Triple<Search, SpaceTrain, Fare> {
    val spaceTrain = spaceTrains.first { it.bound == bound }
    val fare = spaceTrain.fares.first()
    return Triple(selectSpaceTrainWithFare(spaceTrain.number, fare.id), spaceTrain, fare)
}