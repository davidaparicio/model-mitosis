package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journeys
import java.util.UUID
import java.util.UUID.randomUUID

data class Search(val id: UUID = randomUUID(), val criteria: Criteria, val spaceTrains: SpaceTrains) {
    init {
        val (journeys) = criteria

        require(journeys.bounds() `are all satified by a space train amongst` spaceTrains) {
            "some journeys don't have at least one corresponding space train"
        }

        require(spaceTrains `correspond to` journeys) {
            "some space trains don't correspond to any journey from the criteria"
        }
    }

    private fun Journeys.bounds(): List<Bound> = indices.map { Bound.values()[it] }
    private infix fun List<Bound>.`are all satified by a space train amongst`(spaceTrains: SpaceTrains): Boolean {
        return spaceTrains.isEmpty() || spaceTrains.map { it.bound }.containsAll(this)
    }

    private infix fun SpaceTrains.`correspond to`(journeys: Journeys): Boolean {
        return all { it `corresponds to` journeys }
    }

    private infix fun SpaceTrain.`corresponds to`(journeys: Journeys): Boolean {
        return journeys.any { it.departureSpacePort == origin && it.arrivalSpacePort == destination }
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