package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Schedule
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.FareOptions
import java.util.*

data class SpaceTrain(
    val number: String,
    val bound: Bound,
    val originId: String,
    val destinationId: String,
    val schedule: Schedule,
    val fareOptions: FareOptions,
    val compatibleSpaceTrains: Set<String> = emptySet()
) {
    init {
        require(fareOptions.isNotEmpty()) {
            "SpaceTrain must have at least one fare option"
        }

        require(isNotCompatibleWithItself()) {
            "SpaceTrain cannot be compatible with itself"
        }
    }

    fun getFare(fareId: UUID) = this.fareOptions.first { it.id == fareId }
    private fun isNotCompatibleWithItself() = !compatibleSpaceTrains.contains(number)

    companion object {
        operator fun List<SpaceTrain>.get(bound: Bound): List<SpaceTrain> = filter { it.bound == bound }
    }

}
typealias SpaceTrains = List<SpaceTrain>