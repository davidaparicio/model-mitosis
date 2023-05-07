package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxRate
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.amount
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BookingShould : EqualityShould<Booking> {
    private fun SpaceTrain.priced(price: Price): SpaceTrain {
        return this.copy(fare = fare.copy(price = price))
    }

    @Test
    fun `not be created from an empty list of space trains`() {
        assertThatThrownBy { Booking(spaceTrains = listOf()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("cannot book nothing")
    }

    @Test
    fun `compute its total price`(@Random spaceTrain: SpaceTrain, @Random anotherSpaceTrain: SpaceTrain) {
        val booking = Booking(spaceTrains = listOf(spaceTrain, anotherSpaceTrain))
        assertThat(booking.totalPrice).isEqualTo(spaceTrain.fare.price + anotherSpaceTrain.fare.price)
    }

    @Test
    fun `finalize a booking`(@NonFinalized nonFinalizedBooking: Booking) {
        val finalizedBooking = nonFinalizedBooking.finalize()

        assertThat(finalizedBooking.finalized).isTrue
    }

    @Test
    fun `compute its tax portions`(@Random spaceTrain: SpaceTrain) {
        val price = Price(amount("120.00"), REPUBLIC_CREDIT)

        val expectedTaxRate = TaxRate("0.2".toBigDecimal())
        val expectedTaxPortion = TaxPortion(amount("20.00"), REPUBLIC_CREDIT)

        val booking = Booking(spaceTrains = listOf(spaceTrain.priced(price)));

        assertThat(booking.taxRate).isEqualTo(expectedTaxRate)
        assertThat(booking.taxPortion).isEqualTo(expectedTaxPortion)
    }
}