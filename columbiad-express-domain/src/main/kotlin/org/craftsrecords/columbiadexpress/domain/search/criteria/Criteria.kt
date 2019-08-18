package org.craftsrecords.columbiadexpress.domain.search.criteria

data class Criteria(private val journeys: List<Journey>) {
    init {
        require(journeys.isNotEmpty())
        { "Criteria must contain at least one journey" }

        require(journeys.areOrderedByDepartureSchedule())
        { "Criteria must only have journeys ordered by departure schedule" }

        require(journeys.areConnectedTogether())
        { "Criteria must only have connected journeys" }
    }

    private fun List<Journey>.areConnectedTogether() =
            this.zipWithNext { journey, nextJourney -> journey `is connected to` nextJourney }.all { it }

    private fun List<Journey>.areOrderedByDepartureSchedule() =
            this.sortedBy { it.departureSchedule } == this
}
