package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import java.util.UUID
import java.util.UUID.randomUUID

data class Booking(
    val id: UUID = randomUUID(),
    val spaceTrains: List<SpaceTrain>,
    val finalized: Boolean = false,
    ) {

    val totalPrice: Price get() = spaceTrains.map { it.fare.price }.reduce(Price::plus)
    val taxPortion : TaxPortion get() = spaceTrains.map{ it.fare.taxPortion }.reduce(TaxPortion::plus)

    init {
        require(spaceTrains.isNotEmpty()) {
            "cannot book nothing"
        }
    }

    fun finalize(): Booking {
        return copy(finalized = true);
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Booking

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}