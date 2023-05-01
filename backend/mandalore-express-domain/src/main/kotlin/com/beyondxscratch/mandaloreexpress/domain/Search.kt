package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.Bound.values
import java.util.UUID
import java.util.UUID.randomUUID

data class Search(
    val id: UUID = randomUUID(),
    val criteria: Criteria,
    val spaceTrains: SpaceTrains,
) {
    init {
        val (journeys) = criteria

        require(journeys.bounds `are all satified by a space train amongst` spaceTrains) {
            "some journeys don't have at least one corresponding space train"
        }

        require(spaceTrains `correspond to` journeys) {
            "some space trains don't correspond to any journey from the criteria"
        }
    }


    fun getSpaceTrainWithNumber(wantedNumber: String): SpaceTrain = spaceTrains.first { it.number == wantedNumber }

    private val Journeys.bounds
        get(): List<Bound> = indices.map { values()[it] }

    private infix fun List<Bound>.`are all satified by a space train amongst`(spaceTrains: SpaceTrains): Boolean {
        return spaceTrains.isEmpty() || spaceTrains.map { it.bound }.containsAll(this)
    }

    private infix fun SpaceTrains.`correspond to`(journeys: Journeys): Boolean {
        return all { it `corresponds to` journeys }
    }

    private infix fun SpaceTrain.`corresponds to`(journeys: Journeys): Boolean {
        return journeys.any { it.departureSpacePortId == originId && it.arrivalSpacePortId == destinationId }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Search

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
