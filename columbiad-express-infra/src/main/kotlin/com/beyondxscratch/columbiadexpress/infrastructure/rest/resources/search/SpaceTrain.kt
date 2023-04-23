package com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.search

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.Bound
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Fares
import com.beyondxscratch.columbiadexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.time.Duration
import java.time.LocalDateTime

@Resource
data class SpaceTrain(val number: String,
                      val bound: Bound,
                      val originId: String,
                      val destinationId: String,
                      val departureSchedule: LocalDateTime,
                      val arrivalSchedule: LocalDateTime,
                      val duration: Duration,
                      val fares: Fares) : RepresentationModel<SpaceTrain>()

@Resource
data class SpaceTrains(val spaceTrains: List<SpaceTrain>) : RepresentationModel<SpaceTrains>()