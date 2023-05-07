package com.beyondxscratch.mandaloreexpress.domain.anticorruptionlayer

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
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
            val (_, spacetrain, fare) = completeSearchSpaceTrainAndFare
            return SpaceTrain(
                spacetrain.number,
                spacetrain.originId,
                spacetrain.destinationId,
                spacetrain.schedule,
                fare
            )
        }

    private val searchWithIncompleteSelection = baseSearch.copy(id= randomUUID())
    override val incompleteSelectionSearchId = searchWithIncompleteSelection.id

    private val searches = InMemorySearches(listOf(searchWithCompleteSelection, searchWithIncompleteSelection))

    private val searchSelectionForBooking = SearchSelectionForBooking(SpaceTrainsFinder({ setOf() }, searches))

    override val isSelectionComplete = searchSelectionForBooking
    override val retrieveSelection = searchSelectionForBooking
}

