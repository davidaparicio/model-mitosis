package org.craftsrecords.columbiadexpress.domain.search

import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class SpaceTrain(val departureSchedule: LocalDateTime, val arrivalSchedule: LocalDateTime, val fares: Set<Fare>) {
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