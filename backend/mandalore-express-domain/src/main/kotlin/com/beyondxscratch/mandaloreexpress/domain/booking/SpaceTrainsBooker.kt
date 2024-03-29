package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.annotations.DomainService
import com.beyondxscratch.mandaloreexpress.domain.booking.api.FinalizeBooking
import com.beyondxscratch.mandaloreexpress.domain.booking.api.PrepareBooking
import com.beyondxscratch.mandaloreexpress.domain.booking.api.SelectSeatLocation
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SeatLocation
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
    PrepareBooking, SelectSeatLocation, FinalizeBooking {

    override fun `from the selection of`(searchId: UUID): Booking {
        return when {
            !(isSelectionComplete of searchId) -> throw CannotBookAPartialSelection()
            else -> {
                val spaceTrainsToBook = retrieveSelection from searchId
                bookings.save(Booking(spaceTrains = spaceTrainsToBook))
            }
        }

    }

    override fun with(bookingId: UUID): Booking {

        val booking = retrieveBooking(bookingId)

        check(!booking.finalized) {
            "Booking $bookingId is already finalized"
        }

        return bookings.save(booking.finalize())
    }

    override fun selectSeatLocationOnBooking(
        bookingId: UUID,
        spaceTrainNumber: String,
        seatLocation: SeatLocation
    ): Booking {
        return retrieveBooking(bookingId)
            .selectSeatLocationFor(spaceTrainNumber, seatLocation)
            .let { bookings.save(it) }
    }

    private fun retrieveBooking(bookingId: UUID) = ((bookings `find booking identified by` bookingId)
        ?: throw NoSuchElementException("Unknown booking $bookingId"))

}
