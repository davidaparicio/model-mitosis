package com.beyondxscratch.mandaloreexpress.domain.booking.spi

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.SpaceTrain
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.*
import java.util.UUID.randomUUID

interface RetrieveSelectionShould {
    val retrieveSelection: RetrieveSelection
    val selectedSpaceTrain : SpaceTrain
    val completeSelectionSearchId : UUID
    @Test
    fun `throw a NoSuchElementException when there is no search to be retrieved`() {
        val unknownId = randomUUID()

        assertThatThrownBy { retrieveSelection from unknownId }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown search $unknownId")
    }

    @Test
    fun `returns a list of trains selected in the search of the given id`() {
        Assertions.assertThat(retrieveSelection from completeSelectionSearchId).isEqualTo(listOf(selectedSpaceTrain))
    }
}