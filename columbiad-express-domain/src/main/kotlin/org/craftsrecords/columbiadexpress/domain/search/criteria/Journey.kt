package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import java.time.LocalDateTime

data class Journey(private val departureSpacePort: SpacePort,
                   private val departureSchedule: LocalDateTime,
                   private val arrivalSpacePort: SpacePort)