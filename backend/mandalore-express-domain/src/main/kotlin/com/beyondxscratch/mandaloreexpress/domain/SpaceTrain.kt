package com.beyondxscratch.mandaloreexpress.domain

data class SpaceTrain(
    val number: String,
    val bound: Bound,
    val originId: String,
    val destinationId: String,
    val schedule: Schedule
) {


    companion object {
        operator fun List<SpaceTrain>.get(bound: Bound): List<SpaceTrain> = filter { it.bound == bound }
    }

}
typealias SpaceTrains = List<SpaceTrain>


