package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import java.time.Duration
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class SpaceTrain(
        val number: String,
        val bound: Bound,
        val origin: SpacePort,
        val destination: SpacePort,
        val departureSchedule: LocalDateTime,
        val arrivalSchedule: LocalDateTime,
        val fares: Fares) {

    val duration: Duration = between(departureSchedule, arrivalSchedule)

    init {
        require(departureSchedule.isAfter(now())) {
            "departureSchedule cannot be in the past"
        }

        require(arrivalSchedule.isAfter(departureSchedule)) {
            "arrivalSchedule cannot precede the departureSchedule"
        }

        require(fares.isNotEmpty()) {
            "SpaceTrain must have at least one fare"
        }
    }
}

typealias SpaceTrains = List<SpaceTrain>