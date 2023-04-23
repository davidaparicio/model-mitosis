package com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Fare
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.toResource
import java.time.LocalDateTime
import com.beyondxscratch.columbiadexpress.domain.booking.SpaceTrain as DomainSpaceTrain


@Resource
data class SpaceTrain(val number: String,
                      val originId: String,
                      val destinationId: String,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val fare: Fare)

fun List<DomainSpaceTrain>.toResource(): List<SpaceTrain> = map { it.toResource() }

fun DomainSpaceTrain.toResource(): SpaceTrain = SpaceTrain(number, originId, destinationId, schedule.departure, schedule.arrival, fare.toResource())
