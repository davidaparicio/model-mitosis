package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SelectedFare

@DomainService
class SpaceTrainsBooker(
    override val bookings: Bookings
) :
    BookSpaceTrains {

    override fun `from the selection of`(search: Search): Booking {
        return when {
            !search.isSelectionComplete() -> throw CannotBookAPartialSelection()
            else -> {
                val spaceTrainsToBook = convertSelectedSpaceTrainsFrom(search)
                bookings.save(Booking(spaceTrains = spaceTrainsToBook))
            }
        }

    }

    private fun convertSelectedSpaceTrainsFrom(search: Search) =
        search.getSelectedSpaceTrainsSortedByBound()
            .map { selectedSpaceTrain ->
                val (spaceTrain, fareId) = selectedSpaceTrain
                val selectedFareOption = spaceTrain.getFare(fareId)

                return@map SpaceTrain(
                    spaceTrain.number,
                    spaceTrain.originId,
                    spaceTrain.destinationId,
                    spaceTrain.schedule,
                    SelectedFare(selectedFareOption.id, selectedFareOption.comfortClass, selectedFareOption.price)
                )
            }


    private fun Search.getSelectedSpaceTrainsSortedByBound() =
        this.selection.spaceTrainsByBound.sortedBy { it.key.ordinal }
            .map { Pair(this.getSpaceTrainWithNumber(it.value.spaceTrainNumber), it.value.fareId) }
}
