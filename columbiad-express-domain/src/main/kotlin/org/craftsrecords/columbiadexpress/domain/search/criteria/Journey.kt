package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class Journey(val departureSpacePort: SpacePort,
                   val departureSchedule: LocalDateTime,
                   val arrivalSpacePort: SpacePort) {
    init {
        require(departureSpacePort.location != arrivalSpacePort.location)
        { "Cannot create a Journey departing and arriving on the same AstronomicalBody" }

        require(now() < departureSchedule)
        { "Cannot create a Journey with a departure scheduled in the past" }
    }

    infix fun `is connected to`(nextJourney: Journey): Boolean {
        return arrivalSpacePort == nextJourney.departureSpacePort
    }
}