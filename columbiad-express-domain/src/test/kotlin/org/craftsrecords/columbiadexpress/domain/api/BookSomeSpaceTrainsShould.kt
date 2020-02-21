package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.search.OneWay
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.SpaceTrain
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Fare
import org.junit.jupiter.api.Test

interface BookSomeSpaceTrainsShould {
    val bookSomeSpaceTrains: BookSomeSpaceTrains

    @Test
    fun `book some space trains from the selection of`(@OneWay search: Search) {
        val (spaceTrain, fare) = selectSpaceTrainWithFare(search)
        val searchWithSelection = search.selectSpaceTrainWithFare(spaceTrainNumber = spaceTrain.number, fareId = fare.id)

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

    fun selectSpaceTrainWithFare(search: Search): Pair<SpaceTrain, Fare> {
        val spaceTrain = search.spaceTrains.first()
        val fare = spaceTrain.fares.first()
        return Pair(spaceTrain, fare)
    }

    @Test
    fun `not book from an incomplete selection`(@RoundTrip search: Search) {
        val (spaceTrain, fare) = selectSpaceTrainWithFare(search)
        val searchWithPartialSelection = search.selectSpaceTrainWithFare(spaceTrainNumber = spaceTrain.number, fareId = fare.id)

        assertThatThrownBy {
            bookSomeSpaceTrains `from the selection of` searchWithPartialSelection
        }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("cannot book a partial selection")
    }
}