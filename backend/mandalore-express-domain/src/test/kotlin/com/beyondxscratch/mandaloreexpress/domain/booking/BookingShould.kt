package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withFirstClass
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withSecondClass
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.amount
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
    fun `compute its tax portions`(@Random spaceTrain: SpaceTrain, @Random anotherSpaceTrain: SpaceTrain) {
        val firstClassPrice = Price(amount("120.00"), REPUBLIC_CREDIT)
        val secondClassPrice = Price(amount("55.00"), REPUBLIC_CREDIT)

        val expectedTaxPortion = TaxPortion(amount("25.00"), REPUBLIC_CREDIT)

        val booking = Booking(
            spaceTrains = listOf(
                spaceTrain.withFirstClass().priced(firstClassPrice),
                anotherSpaceTrain.withSecondClass().priced(secondClassPrice)
            )
        )

        assertThat(booking.taxPortion).isEqualTo(expectedTaxPortion)
    }
}