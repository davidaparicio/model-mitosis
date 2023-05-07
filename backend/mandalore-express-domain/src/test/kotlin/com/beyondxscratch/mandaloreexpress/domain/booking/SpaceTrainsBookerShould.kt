package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.FakeIsSelectionComplete
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.FakeRetrieveSelection
import com.beyondxscratch.mandaloreexpress.domain.booking.stubs.InMemoryBookings
import java.util.*
import java.util.UUID.randomUUID

class SpaceTrainsBookerShould(
    private val spaceTrain: SpaceTrain
) : BookSpaceTrainsShould {
        override val completeSelectionSearchId: UUID = randomUUID()
        override val uncompleteSelectionSearchId: UUID = randomUUID()
        override val selectedSpaceTrains: List<SpaceTrain> = listOf(spaceTrain)


    private val spaceTrainsBooker: SpaceTrainsBooker
        get() {
            return SpaceTrainsBooker(
                FakeIsSelectionComplete(mapOf(completeSelectionSearchId to true, uncompleteSelectionSearchId to false)),
                FakeRetrieveSelection(mapOf(completeSelectionSearchId to listOf(spaceTrain))),
                InMemoryBookings()
            )
        }

    override val bookSpaceTrains: BookSpaceTrains
        get() = spaceTrainsBooker
    
}