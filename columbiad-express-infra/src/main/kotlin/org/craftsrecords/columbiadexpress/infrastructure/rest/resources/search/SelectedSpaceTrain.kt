package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.search

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fare
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.SpacePort
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

@Resource
data class SelectedSpaceTrain(val number: String,
                              val bound: Bound,
                              val origin: SpacePort,
                              val destination: SpacePort,
                              val departureSchedule: LocalDateTime,
                              val arrivalSchedule: LocalDateTime,
                              val fare: Fare) : RepresentationModel<SelectedSpaceTrain>()