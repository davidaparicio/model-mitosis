package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fares
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Schedule

data class SpaceTrain(
        val number: String,
        val bound: Bound,
        val originId: String,
        val destinationId: String,
        val schedule: Schedule,
        val fares: Fares,
        val compatibleSpaceTrains: Set<String> = emptySet()) {

    init {
        require(fares.isNotEmpty()) {
            "SpaceTrain must have at least one fare"
        }

        require(isNotCompatibleWithItself()) {
            "SpaceTrain cannot be compatible with itself"
        }
    }

    private fun isNotCompatibleWithItself() = !compatibleSpaceTrains.contains(number)

    companion object {
        operator fun List<SpaceTrain>.get(bound: Bound): List<SpaceTrain> = filter { it.bound == bound }
    }

}
typealias SpaceTrains = List<SpaceTrain>


