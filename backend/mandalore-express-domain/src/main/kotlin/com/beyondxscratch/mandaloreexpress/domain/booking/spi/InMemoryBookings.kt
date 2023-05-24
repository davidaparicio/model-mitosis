package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import com.beyondxscratch.mandaloreexpress.annotations.Stub
import com.beyondxscratch.mandaloreexpress.domain.booking.Booking
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Schedule
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Amount
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Currency.REPUBLIC_CREDIT
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SelectedFare
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.fare.Price
import java.math.BigDecimal
import java.time.LocalDateTime.now
import java.util.UUID

@Stub
class InMemoryBookings : Bookings {

    val bookings: MutableMap<UUID, Booking> = hashMapOf()

    override fun `find booking identified by`(bookingId: UUID): Booking? {
        if (bookingId == UUID.fromString("19afd6be-d136-4abc-bf30-916ffea32df1")) {
            return Booking(
                spaceTrains = listOf(
                    SpaceTrain(
                        "MANDA250",
                        "http://localhost:1865/spaceports/f01ed70b-513e-3bef-98f4-19038a4f6d64",
                        "http://localhost:1865/spaceports/4c542529-4427-3f3c-90d8-b47cdaa8e20a",
                        Schedule(now().minusWeeks(2), now().minusWeeks(1)),
                        SelectedFare(
                            comfortClass = ComfortClass.FIRST,
                            price = Price(Amount(BigDecimal(162)), REPUBLIC_CREDIT)
                        )
                    )
                )
            )
        }
        return bookings[bookingId]
    }

    override fun save(booking: Booking): Booking {
        bookings[booking.id] = booking
        return booking
    }
}