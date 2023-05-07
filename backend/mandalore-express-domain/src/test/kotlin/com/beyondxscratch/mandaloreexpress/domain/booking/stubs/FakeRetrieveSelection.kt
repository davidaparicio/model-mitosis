package com.beyondxscratch.mandaloreexpress.domain.booking.stubs

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.RetrieveSelection
import java.util.*

class FakeRetrieveSelection(private val selections : Map<UUID, List<SpaceTrain>>) : RetrieveSelection {
    override fun from(searchId: UUID): List<SpaceTrain> {
        return selections[searchId] ?: throw NoSuchElementException("Unknown search $searchId")
    }
}