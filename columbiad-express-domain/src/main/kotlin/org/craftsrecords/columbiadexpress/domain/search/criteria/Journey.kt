package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class Journey(private val departureSpacePort: SpacePort,
                   private val departureSchedule: LocalDateTime,
                   private val arrivalSpacePort: SpacePort) {

    init {
        require(departureSpacePort.location != arrivalSpacePort.location)
        { "Cannot create a Journey departing and arriving on the same AstronomicalBody" }

        require(now() < departureSchedule)
        { "Cannot create a Journey with a departure scheduled in the past" }
    }
}