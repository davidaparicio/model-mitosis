package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.criteria.oneWayCriteria
import com.beyondxscratch.mandaloreexpress.domain.criteria.roundTripCriteria
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Fare
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.spaceTrainsFrom
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
    val alreadySelectedSpaceTrain = spaceTrains.find { it.number == selection[bound]?.spaceTrainNumber }
    val spaceTrainToSelect = alreadySelectedSpaceTrain?.let { spaceTrains.first { it.number == it.compatibleSpaceTrains.first() } }
            ?: spaceTrains.first { it.bound == bound }
    val fare = spaceTrainToSelect.fares.first()
    return Triple(selectSpaceTrainWithFare(spaceTrainToSelect.number, fare.id, false), spaceTrainToSelect, fare)
}