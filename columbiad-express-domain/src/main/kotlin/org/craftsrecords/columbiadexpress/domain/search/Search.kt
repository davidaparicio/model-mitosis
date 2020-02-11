package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journeys
import org.craftsrecords.columbiadexpress.domain.search.selection.SelectedSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.selection.Selection
import java.util.*
import java.util.UUID.randomUUID

data class Search(
        val id: UUID = randomUUID(),
        val criteria: Criteria,
        val spaceTrains: SpaceTrains,
        val selection: Selection = Selection()
) {
    init {
        val (journeys) = criteria
        val (selectedSpaceTrains) = selection

        require(journeys.bounds `are all satified by a space train amongst` spaceTrains) {
            "some journeys don't have at least one corresponding space train"
        }

        require(spaceTrains `correspond to` journeys) {
            "some space trains don't correspond to any journey from the criteria"
        }

        require(selectedSpaceTrains `exist in` spaceTrains) {
            "unknown space train in the selection"
        }

        require(selectedSpaceTrains `with only known fares from` spaceTrains) {
            "unknown fare in the selection"
        }

        require(selectedSpaceTrains `are corresponding to the bounds of` spaceTrains) {
            "selected space trains don't correspond to the right bound"
        }

    }


    private infix fun Map<Bound, SelectedSpaceTrain>.`exist in`(spaceTrains: SpaceTrains): Boolean =
            spaceTrains.map { it.number }.containsAll(this.values.map { it.spaceTrainNumber })

    private infix fun Map<Bound, SelectedSpaceTrain>.`with only known fares from`(spaceTrains: SpaceTrains): Boolean =
            this.values.all { chosenSpaceTrain ->
                spaceTrains.find { it.number == chosenSpaceTrain.spaceTrainNumber }?.fares?.any { it.id == chosenSpaceTrain.fareId }
                        ?: false
            }

    private infix fun Map<Bound, SelectedSpaceTrain>.`are corresponding to the bounds of`(spaceTrains: SpaceTrains): Boolean =
            this.all { spaceTrains.find { spaceTrain -> spaceTrain.number == it.value.spaceTrainNumber }?.bound == it.key }

    private val Journeys.bounds
        get(): List<Bound> = indices.map { Bound.values()[it] }

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

    fun selectSpaceTrainWithFare(spaceTrainNumber: String, fareId: UUID): Search {
        val spaceTrain = spaceTrains.first { it.number == spaceTrainNumber }
        val price = spaceTrain.fares.first { it.id == fareId }.price
        val newSelection = selection.selectSpaceTrainWithFare(spaceTrain, fareId, price)
        return this.copy(selection = newSelection)
    }
}