package com.beyondxscratch.mandaloreexpress.domain.booking.api

import com.beyondxscratch.mandaloreexpress.domain.booking.CannotBookAPartialSelection
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare.Price
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.*
import java.util.UUID.*
import kotlin.NoSuchElementException

interface BookSpaceTrainsShould {
    val bookSpaceTrains: BookSpaceTrains
    val completeSelectionSearchId: UUID
    val uncompleteSelectionSearchId: UUID
    val selectedSpaceTrains: List<SpaceTrain>

    @Test
    fun `book some space trains from the selection of`() {

        val booking = bookSpaceTrains `from the selection of` completeSelectionSearchId

        assertThat(booking.totalPrice).isEqualTo(selectedSpaceTrains.map { it.fare.price }.reduce(Price::plus))
        assertThat(booking.spaceTrains).isEqualTo(selectedSpaceTrains)
    }

    @Test
    fun `not book from an incomplete selection`() {

        assertThatThrownBy {
            bookSpaceTrains `from the selection of` uncompleteSelectionSearchId
        }.isInstanceOf(CannotBookAPartialSelection::class.java)
            .hasMessage("cannot book a partial selection")
    }

    @Test
    fun `throw NoSuchElementException when trying book an unknown search`() {

        val unknownId = randomUUID()
        assertThatThrownBy {
            bookSpaceTrains `from the selection of` unknownId
        }.isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown search $unknownId")
    }
}