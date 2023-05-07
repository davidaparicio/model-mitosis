package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search.toResource
import java.time.LocalDateTime
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain as DomainSpaceTrain


@Resource
data class SpaceTrain(val number: String,
                      val originId: String,
                      val destinationId: String,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val selectedFare: SelectedFare
)

fun List<DomainSpaceTrain>.toResource(): List<SpaceTrain> = map { it.toResource() }

fun DomainSpaceTrain.toResource(): SpaceTrain =
    SpaceTrain(number, originId, destinationId, schedule.departure, schedule.arrival, selectedFare.toResource())
