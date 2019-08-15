package org.craftsrecords.columbiadexpress.domain.search.criteria

data class Criteria(private val journeys: Set<Journey>) {
    init {
        require(journeys.isNotEmpty())
        { "Criteria must contain at least one journey" }

        require(journeys.areOrderedByDepartureSchedule())
        { "Criteria must only have journeys ordered by departure schedule" }

        require(journeys.areConnectedTogether())
        { "Criteria must only have connected journeys" }
    }

    private fun Set<Journey>.areConnectedTogether() =
            this.zipWithNext { journey, nextJourney -> journey `is connected to` nextJourney }.all { it }

    private fun Set<Journey>.areOrderedByDepartureSchedule() =
            this.sortedBy { it.departureSchedule } == this.toList()
}
