package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fares
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Schedule
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort

data class SpaceTrain(
        val number: String,
        val bound: Bound,
        val origin: SpacePort,
        val destination: SpacePort,
        val schedule: Schedule,
        val fares: Fares) {


    init {
        require(fares.isNotEmpty()) {
            "SpaceTrain must have at least one fare"
        }
    }
}

typealias SpaceTrains = List<SpaceTrain>