package com.beyondxscratch.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.beyondxscratch.columbiadexpress.domain.CannotBookAPartialSelection
import com.beyondxscratch.columbiadexpress.domain.search.OneWay
import com.beyondxscratch.columbiadexpress.domain.search.RoundTrip
import com.beyondxscratch.columbiadexpress.domain.search.Search
import com.beyondxscratch.columbiadexpress.domain.search.selectAnInboundSpaceTrain
import com.beyondxscratch.columbiadexpress.domain.search.selectAnOutboundSpaceTrain
import org.junit.jupiter.api.Test

interface BookSpaceTrainsShould {
    val bookSpaceTrains: BookSpaceTrains

    @Test
    fun `book some space trains from the selection of`(@OneWay search: Search) {
        val (searchWithSelection, spaceTrain, fare) = search.selectAnOutboundSpaceTrain()

        val booking = bookSpaceTrains `from the selection of` searchWithSelection

        assertThat(booking.totalPrice).isEqualTo(fare.price)
        assertThat(booking.spaceTrains).hasSize(1)

        val selectedSpaceTrain = booking.spaceTrains[0]
        assertThat(selectedSpaceTrain.fare).isEqualTo(fare)
        assertThat(selectedSpaceTrain.destinationId).isEqualTo(spaceTrain.destinationId)
        assertThat(selectedSpaceTrain.originId).isEqualTo(spaceTrain.originId)
        assertThat(selectedSpaceTrain.number).isEqualTo(spaceTrain.number)
        assertThat(selectedSpaceTrain.schedule).isEqualTo(spaceTrain.schedule)
    }

    @Test
    fun `not book from an incomplete selection`(@RoundTrip search: Search) {
        val (searchWithPartialSelection) = search.selectAnOutboundSpaceTrain()

        assertThatThrownBy {
            bookSpaceTrains `from the selection of` searchWithPartialSelection
        }.isInstanceOf(CannotBookAPartialSelection::class.java)
            .hasMessage("cannot book a partial selection")
    }

    @Test
    fun `preserve bound order in selection`(@RoundTrip search: Search) {
        val (searchWithSelection) = search.selectAnInboundSpaceTrain().selectAnOutboundSpaceTrain()

        val booking = bookSpaceTrains `from the selection of` searchWithSelection

        val expectedSortedSpaceTrainNumber =
            searchWithSelection.selection
                .spaceTrainsByBound
                .sortedBy { it.key.ordinal }
                .map { it.value.spaceTrainNumber }

        assertThat(booking.spaceTrains).extracting("number").containsExactlyElementsOf(expectedSortedSpaceTrainNumber)
    }
}