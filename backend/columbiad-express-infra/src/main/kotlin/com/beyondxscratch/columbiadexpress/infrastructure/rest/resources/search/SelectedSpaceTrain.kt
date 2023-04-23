package com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.search

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Fare
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Resource
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