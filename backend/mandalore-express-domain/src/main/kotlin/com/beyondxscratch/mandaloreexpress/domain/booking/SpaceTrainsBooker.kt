package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.Booking
import com.beyondxscratch.mandaloreexpress.domain.Search
import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Fare
import com.beyondxscratch.mandaloreexpress.domain.spi.Bookings

@DomainService
class SpaceTrainsBooker(
    override val bookings: Bookings
) :
    BookSpaceTrains {
    override fun `from the selection of`(search: Search): Booking {
        return when {
            !search.isSelectionComplete() -> throw IllegalStateException("cannot book a partial selection")
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
                val fare = spaceTrain.getFare(fareId)

                return@map SpaceTrain(
                    spaceTrain.number,
                    spaceTrain.bound,
                    spaceTrain.originId,
                    spaceTrain.destinationId,
                    spaceTrain.schedule,
                    setOf<Fare>(fare),
                    spaceTrain.compatibleSpaceTrains
                )
            }


    private fun Search.getSelectedSpaceTrainsSortedByBound() =
        this.selection.spaceTrainsByBound.sortedBy { it.key.ordinal }
            .map { Pair(this.getSpaceTrainWithNumber(it.value.spaceTrainNumber), it.value.fareId) }
}
