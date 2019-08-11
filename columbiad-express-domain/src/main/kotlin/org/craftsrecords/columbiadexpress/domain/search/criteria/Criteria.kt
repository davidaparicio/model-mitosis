package org.craftsrecords.columbiadexpress.domain.search.criteria

data class Criteria(private val journeys: Set<Journey>) {
    init {
        require(journeys.isNotEmpty()) { "Criteria must contain at least one journey" }

        require(journeys.sortedBy { it.departureSchedule }.toList() == journeys.toList())
        { "Criteria must only have journeys ordered by departure schedule" }

        require(journeys.foldIndexed(true,
                { index, acc, journey ->
                    acc && journeys.elementAtOrNull(index + 1)?.let { journey `is connected to` it } ?: true
                }))
        { "Criteria must only have connected journeys" }
    }
}
