package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.search.Bound
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

@Resource
data class SpaceTrain(val number: String,
                      val bound: Bound,
                      val origin: SpacePort,
                      val destination: SpacePort,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val fares: Fares) : RepresentationModel<SpaceTrain>()

@Resource
data class SpaceTrains(val spaceTrains: List<SpaceTrain>) : CollectionModel<SpaceTrains>()