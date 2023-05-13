package com.beyondxscratch.mandaloreexpress.domain.anticorruptionlayer

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.Schedule
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Price
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SelectedFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.IsSelectionCompleteShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.RetrieveSelectionShould
import com.beyondxscratch.mandaloreexpress.domain.search.*
import com.beyondxscratch.mandaloreexpress.domain.search.stubs.InMemorySearches
import java.util.UUID.randomUUID


class SearchSelectionForBookingShould(
    @OneWay private val baseSearch: Search
) : IsSelectionCompleteShould, RetrieveSelectionShould {

    private val completeSearchSpaceTrainAndFare = baseSearch.selectAnOutboundSpaceTrain()

    private val searchWithCompleteSelection = completeSearchSpaceTrainAndFare.first
    override val completeSelectionSearchId = searchWithCompleteSelection.id

    override val selectedSpaceTrain: SpaceTrain
        get() {
            val (_, spacetrain, fareOption) = completeSearchSpaceTrainAndFare
            val (_, comfortClass, price) = fareOption
            return SpaceTrain(
                spacetrain.number,
                spacetrain.originId,
                spacetrain.destinationId,
                Schedule(spacetrain.schedule.departure, spacetrain.schedule.arrival),
                SelectedFare(fareOption.id, ComfortClass.valueOf(comfortClass.name), Price(price.amount, price.currency))
            )
        }

    private val searchWithIncompleteSelection = baseSearch.copy(id= randomUUID())
    override val incompleteSelectionSearchId = searchWithIncompleteSelection.id

    private val searches = InMemorySearches(listOf(searchWithCompleteSelection, searchWithIncompleteSelection))

    private val searchSelectionForBooking = SearchSelectionForBooking(SpaceTrainsFinder({ setOf() }, searches))

    override val isSelectionComplete = searchSelectionForBooking
    override val retrieveSelection = searchSelectionForBooking
}

