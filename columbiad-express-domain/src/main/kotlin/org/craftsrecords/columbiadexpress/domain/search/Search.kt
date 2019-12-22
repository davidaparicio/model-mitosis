package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import java.util.UUID
import java.util.UUID.randomUUID

data class Search(val id: UUID = randomUUID(), val criteria: Criteria, val spaceTrains: SpaceTrains) {
    init {
        require(spaceTrains.all { it `corresponds to` criteria.journeys }) {
            "some space trains don't correspond to any journey from the criteria"
        }
    }

    private infix fun SpaceTrain.`corresponds to`(journeys: List<Journey>): Boolean {
        //criteria.journeys.indices.map { journeys. }
        return journeys.any { it.departureSpacePort == this.origin && it.arrivalSpacePort == this.destination }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Search

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}