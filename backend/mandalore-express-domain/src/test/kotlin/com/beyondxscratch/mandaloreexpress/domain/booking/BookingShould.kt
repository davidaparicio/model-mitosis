package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.departing
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.priced
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.spaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withFirstClass
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.withSecondClass
import com.beyondxscratch.mandaloreexpress.domain.booking.tax.TaxPortion
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.amount
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class BookingShould : EqualityShould<Booking> {
    @Test
    fun `not be created from an empty list of space trains`() {
        assertThatThrownBy { Booking(spaceTrains = listOf()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("cannot book nothing")
    }

    @Test
    fun `allow a schedule in the past`(@Finalized baseBooking: Booking) {
        val lastWeek = now().minusWeeks(1)

        assertThatCode { baseBooking.copy(spaceTrains = listOf(spaceTrain().departing(lastWeek))) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `can not create new booking with spacetrains departing in the past`(@NonFinalized baseBooking: Booking){
        val spaceTrain = spaceTrain().departing(now().minusDays(2))

        assertThatThrownBy{Booking(spaceTrains = listOf(spaceTrain))}
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SpaceTrains cannot depart in the past for a new Booking")
    }

    @Test
    fun `compute its total price`(@Random spaceTrain: SpaceTrain, @Random anotherSpaceTrain: SpaceTrain) {
        val booking = Booking(spaceTrains = listOf(spaceTrain, anotherSpaceTrain))
        assertThat(booking.totalPrice).isEqualTo(spaceTrain.selectedFare.price + anotherSpaceTrain.selectedFare.price)
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