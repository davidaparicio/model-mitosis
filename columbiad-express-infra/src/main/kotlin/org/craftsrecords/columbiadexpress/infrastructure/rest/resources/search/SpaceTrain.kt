package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fares
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.SpacePort
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import java.time.Duration
import java.time.LocalDateTime

@Resource
data class SpaceTrain(val number: String,
                      val bound: Bound,
                      val origin: SpacePort,
                      val destination: SpacePort,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val duration: Duration,
                      val fares: Fares) : RepresentationModel<SpaceTrain>()

@Resource
data class SpaceTrains(val spaceTrains: List<SpaceTrain>) : CollectionModel<SpaceTrains>()