package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.CannotBookAPartialSelection
import org.craftsrecords.columbiadexpress.domain.search.OneWay
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.selectAnInboundSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.selectAnOutboundSpaceTrain
import org.junit.jupiter.api.Test

interface BookSomeSpaceTrainsShould {
    val bookSomeSpaceTrains: BookSomeSpaceTrains

    @Test
    fun `book some space trains from the selection of`(@OneWay search: Search) {
        val (searchWithSelection, spaceTrain, fare) = search.selectAnOutboundSpaceTrain()

        val booking = bookSomeSpaceTrains `from the selection of` searchWithSelection

        assertThat(booking.totalPrice).isEqualTo(fare.price)
        assertThat(booking.spaceTrains).hasSize(1)

        val selectedSpaceTrain = booking.spaceTrains[0]
        assertThat(selectedSpaceTrain.fare).isEqualTo(fare)
        assertThat(selectedSpaceTrain.destination).isEqualTo(spaceTrain.destination)
        assertThat(selectedSpaceTrain.origin).isEqualTo(spaceTrain.origin)
        assertThat(selectedSpaceTrain.number).isEqualTo(spaceTrain.number)
        assertThat(selectedSpaceTrain.schedule).isEqualTo(spaceTrain.schedule)
    }

    @Test
    fun `not book from an incomplete selection`(@RoundTrip search: Search) {
        val (searchWithPartialSelection) = search.selectAnOutboundSpaceTrain()

        assertThatThrownBy {
            bookSomeSpaceTrains `from the selection of` searchWithPartialSelection
        }.isInstanceOf(CannotBookAPartialSelection::class.java)
                .hasMessage("cannot book a partial selection")
    }

    @Test
    fun `preserve bound order in selection`(@RoundTrip search: Search) {
        val (searchWithSelection) = search.selectAnInboundSpaceTrain().selectAnOutboundSpaceTrain()

        val booking = bookSomeSpaceTrains `from the selection of` searchWithSelection

        val expectedSortedSpaceTrainNumber =
                searchWithSelection.selection.selectedSpaceTrains
                        .entries
                        .sortedBy { it.key.ordinal }
                        .map { it.value.spaceTrainNumber }

        assertThat(booking.spaceTrains).extracting("number").containsExactlyElementsOf(expectedSortedSpaceTrainNumber)
    }
}