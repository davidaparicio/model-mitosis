package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import java.util.UUID
import java.util.UUID.randomUUID


data class Booking(
    val id: UUID = randomUUID(),
    val spaceTrains: List<SpaceTrain>,
    val finalized: Boolean = false,
    val selectedSeatLocations: Map<SpaceTrain, SeatLocation> = spaceTrains.associateWith { SeatLocation.ANY }
    ) {

    val totalPrice: Price get() = spaceTrains.map { it.fare.price }.reduce(Price::plus)

    init {
        require(spaceTrains.isNotEmpty()) {
            "cannot book nothing"
        }

        require(selectedSeatLocationsCompatibleWithSpaceTrains()) {
            "SelectedSeatLocations are incompatible with the SpaceTrains"
        }
    }


    private fun selectedSeatLocationsCompatibleWithSpaceTrains(): Boolean {
        return selectedSeatLocations
             .all { (spaceTrain, seatLocation) -> spaceTrain.fare.isCompatibleWith(seatLocation) }

    }

    fun finalize(): Booking {
        return copy(finalized = true);
    }

    fun selectSeatLocationFor(spaceTrainNumber: String, seatLocation: SeatLocation): Booking {
        check(!finalized) {
            "Cannot do a seatLocation selection on a finalized booking"
        }
        val spaceTrain = spaceTrains.first{ it.number == spaceTrainNumber}
        return copy(
            selectedSeatLocations = selectedSeatLocations.plus(spaceTrain to seatLocation)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Booking

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}