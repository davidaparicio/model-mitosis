package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BookingShould : EqualityShould<Booking> {
    @Test
    fun `not be created from an empty list of space trains`() {
        assertThatThrownBy { Booking(spaceTrains = listOf()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("cannot book nothing")
    }

    @Test
    fun `compute its total price`(@Random spaceTrain: SpaceTrain, @Random anotherSpaceTrain: SpaceTrain) {
        val booking = Booking(spaceTrains = listOf(spaceTrain, anotherSpaceTrain))
        assertThat(booking.totalPrice).isEqualTo(spaceTrain.fares.first().price + anotherSpaceTrain.fares.first().price)
    }
}