package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.oneWayCriteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.roundTripCriteria
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