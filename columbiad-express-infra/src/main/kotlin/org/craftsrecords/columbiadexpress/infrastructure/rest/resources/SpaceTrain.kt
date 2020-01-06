package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.search.Bound
import java.time.LocalDateTime
import java.util.UUID
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain as DomainSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrains as DomainSpaceTrains

@Resource
class SpaceTrain(val id: UUID,
                 val number: String,
                 val bound: Bound,
                 val origin: SpacePort,
                 val destination: SpacePort,
                 val departureSchedule: LocalDateTime,
                 val arrivalSchedule: LocalDateTime,
                 val fares: Fares)

typealias SpaceTrains = List<SpaceTrain>


fun DomainSpaceTrain.toResource(): SpaceTrain = SpaceTrain(
        id,
        number,
        bound,
        origin.toResource(),
        destination.toResource(),
        departureSchedule,
        arrivalSchedule,
        fares.toResource())

fun DomainSpaceTrains.toResource(): SpaceTrains = this.map { it.toResource() }