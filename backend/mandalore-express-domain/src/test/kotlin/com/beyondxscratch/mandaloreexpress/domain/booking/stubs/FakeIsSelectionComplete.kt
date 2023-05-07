package com.beyondxscratch.mandaloreexpress.domain.booking.stubs

import com.beyondxscratch.mandaloreexpress.domain.booking.spi.IsSelectionComplete
import java.util.*

class FakeIsSelectionComplete(private val selections : Map<UUID, Boolean>) : IsSelectionComplete {
    override fun of(searchId: UUID): Boolean {
        return selections[searchId] ?: throw NoSuchElementException("Unknown search $searchId")
    }

}