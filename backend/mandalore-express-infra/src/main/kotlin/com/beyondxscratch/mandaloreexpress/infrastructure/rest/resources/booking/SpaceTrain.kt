package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.fare.Fare
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.fare.toResource
import java.time.LocalDateTime
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain as DomainSpaceTrain


@Resource
data class SpaceTrain(val number: String,
                      val originId: String,
                      val destinationId: String,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val fare: Fare)

fun List<DomainSpaceTrain>.toResource(): List<SpaceTrain> = map { it.toResource() }

fun DomainSpaceTrain.toResource(): SpaceTrain =
    SpaceTrain(number, originId, destinationId, schedule.departure, schedule.arrival, fares.first().toResource())
