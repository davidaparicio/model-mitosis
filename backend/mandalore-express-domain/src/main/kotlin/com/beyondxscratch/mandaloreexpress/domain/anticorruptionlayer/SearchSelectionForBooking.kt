package com.beyondxscratch.mandaloreexpress.domain.anticorruptionlayer

import com.beyondxscratch.mandaloreexpress.annotations.AntiCorruptionLayer
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Fare as BookingFare
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.IsSelectionComplete
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.RetrieveSelection
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.Fare as SearchFare
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrains
import java.util.UUID

@AntiCorruptionLayer
class SearchSelectionForBooking(private val searchForSpaceTrains: SearchForSpaceTrains) : RetrieveSelection,
    IsSelectionComplete {
    override fun of(searchId: UUID): Boolean {
        val search = searchForSpaceTrains `identified by` searchId
        return search.isSelectionComplete()
    }

    override fun from(searchId: UUID): List<SpaceTrain> {
        val search = searchForSpaceTrains `identified by` searchId
        return convertSelectedSpaceTrainsFrom(search)
    }

    private fun convertSelectedSpaceTrainsFrom(search: Search) =
        search.getSelectedSpaceTrainsSortedByBound()
            .map { selectedSpaceTrain ->
                val (spaceTrain, fareId) = selectedSpaceTrain
                val fare = spaceTrain.getFare(fareId)

                return@map SpaceTrain(
                    spaceTrain.number,
                    spaceTrain.originId,
                    spaceTrain.destinationId,
                    spaceTrain.schedule,
                    convertToBookingFare(fare),
                )
            }

    private fun convertToBookingFare(fare: SearchFare): BookingFare {
        val comfortClass = ComfortClass.valueOf(fare.comfortClass.name)

        return BookingFare(
            id = fare.id,
            comfortClass = comfortClass,
            price = fare.price,
        )
    }

    private fun Search.getSelectedSpaceTrainsSortedByBound() =
        selection.spaceTrainsByBound.sortedBy { it.key.ordinal }
            .map {
                Pair(
                    getSpaceTrainWithNumber(it.value.spaceTrainNumber),
                    it.value.fareId
                )
            }
}