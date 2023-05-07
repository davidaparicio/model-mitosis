package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.Bookings
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.IsSelectionComplete
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.RetrieveSelection
import java.util.*

@DomainService
class SpaceTrainsBooker(
    override val isSelectionComplete: IsSelectionComplete,
    override val retrieveSelection: RetrieveSelection,
    override val bookings: Bookings
) :
    BookSpaceTrains {

    override fun `from the selection of`(searchId: UUID): Booking {
        return when {
            !(isSelectionComplete of searchId) -> throw CannotBookAPartialSelection()
            else -> {
                val spaceTrainsToBook = retrieveSelection from searchId
                bookings.save(Booking(spaceTrains = spaceTrainsToBook))
            }
        }

    }

}
