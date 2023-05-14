package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search

import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

@Resource
data class SelectedSpaceTrain(val number: String,
                              val bound: Bound,
                              val originId: String,
                              val destinationId: String,
                              val departureSchedule: LocalDateTime,
                              val arrivalSchedule: LocalDateTime,
                              val fare: Fare) : RepresentationModel<SelectedSpaceTrain>()