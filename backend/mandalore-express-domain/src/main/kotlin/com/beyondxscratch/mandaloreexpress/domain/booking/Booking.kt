package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxRate
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import java.math.BigDecimal
import java.util.UUID
import java.util.UUID.randomUUID

private val TAX_RATE : TaxRate = TaxRate(BigDecimal("0.2"))

data class Booking(
    val id: UUID = randomUUID(),
    val spaceTrains: List<SpaceTrain>,
    val finalized: Boolean = false,
    ) {

    val totalPrice: Price get() = spaceTrains.map { it.fare.price }.reduce(Price::plus)
    val taxRate : TaxRate = TAX_RATE
    val taxPortion : TaxPortion get() = TODO("Compute the tax portion")

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