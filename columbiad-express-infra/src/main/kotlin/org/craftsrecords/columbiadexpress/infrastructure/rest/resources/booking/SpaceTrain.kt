package org.craftsrecords.columbiadexpress.infrastructure.rest.resources.booking

import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Fare
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.Resource
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.spaceport.toResource
import org.craftsrecords.columbiadexpress.infrastructure.rest.resources.toResource
import java.time.LocalDateTime
import org.craftsrecords.columbiadexpress.domain.booking.SpaceTrain as DomainSpaceTrain


@Resource
data class SpaceTrain(val number: String,
                      val origin: SpacePort,
                      val destination: SpacePort,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val fare: Fare)

fun List<DomainSpaceTrain>.toResource(): List<SpaceTrain> = map { it.toResource() }

fun DomainSpaceTrain.toResource(): SpaceTrain = SpaceTrain(number, origin.toResource(), destination.toResource(), schedule.departure, schedule.arrival, fare.toResource())
