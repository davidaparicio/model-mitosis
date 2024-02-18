package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.api.*
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.FakeIsSelectionComplete
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.FakeRetrieveSelection
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.InMemoryBookings
import java.util.UUID
import java.util.UUID.randomUUID

class SpaceTrainsBookerShould(
    private val spaceTrain: SpaceTrain
) : PrepareBookingShould, FinalizeBookingShould {
    override val completeSelectionSearchId: UUID = randomUUID()
    override val uncompleteSelectionSearchId: UUID = randomUUID()
    override val selectedSpaceTrains: List<SpaceTrain> = listOf(spaceTrain)
    override val bookings = InMemoryBookings()
    override val finalizeBooking: FinalizeBooking
        get() = spaceTrainsBooker

    override val prepareBooking: PrepareBooking
        get() = spaceTrainsBooker

    private val spaceTrainsBooker: SpaceTrainsBooker
        get() {
            return SpaceTrainsBooker(
                FakeIsSelectionComplete(mapOf(completeSelectionSearchId to true, uncompleteSelectionSearchId to false)),
                FakeRetrieveSelection(mapOf(completeSelectionSearchId to listOf(spaceTrain))),
                bookings
            )
        }

}