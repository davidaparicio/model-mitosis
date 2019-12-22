package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import java.util.UUID
import java.util.UUID.randomUUID
import org.craftsrecords.columbiadexpress.domain.search.Journey as JourneyOrder

data class Search(val id: UUID = randomUUID(), val criteria: Criteria, val spaceTrains: SpaceTrains) {
    init {
        require(allJourneysToHaveAtLeastOneSpaceTrain()) {
            "some journeys don't have at least one corresponding space train"
        }

        require(spaceTrains.all { it `corresponds to` criteria.journeys }) {
            "some space trains don't correspond to any journey from the criteria"
        }
    }

    private fun allJourneysToHaveAtLeastOneSpaceTrain() =
            criteria.journeys.indices
                    .map { JourneyOrder.values()[it] }
                    .all { journey ->
                        spaceTrains.any { spaceTrain -> spaceTrain.journey == journey }
                    }

    private infix fun SpaceTrain.`corresponds to`(journeys: List<Journey>): Boolean {
        return journeys.any { it.departureSpacePort == this.origin && it.arrivalSpacePort == this.destination }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Search

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = id.hashCode()
}