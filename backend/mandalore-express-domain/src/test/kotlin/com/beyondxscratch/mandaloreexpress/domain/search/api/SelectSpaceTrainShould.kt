package com.beyondxscratch.mandaloreexpress.domain.search.api

import com.beyondxscratch.mandaloreexpress.domain.annotations.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.selection.SelectedSpaceTrain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.data.MapEntry.entry
import org.junit.jupiter.api.Test
import java.util.UUID.nameUUIDFromBytes
import java.util.UUID.randomUUID

interface SelectSpaceTrainShould {
    val selectSpaceTrain: SelectSpaceTrain

    @Test
    fun `select a space train with a specific fare in an existing search`(@RoundTrip search: Search) {
        val spaceTrain = search.spaceTrains.first()
        val fare = spaceTrain.fares.first()

        val result = selectSpaceTrain `having the number` spaceTrain.number `with the fare` fare.id `in search` search.id `by resetting the selection` false
        assertThat(result.selection)
                .containsOnly(entry(spaceTrain.bound, SelectedSpaceTrain(spaceTrain.number, fare.id, fare.price)))
    }

    @Test
    fun `throw an exception if search id is unknown`() {
        val spaceTrainNumber = "random"
        val fareId = randomUUID()
        val searchId = nameUUIDFromBytes("unknown".toByteArray())

        assertThatThrownBy { selectSpaceTrain `having the number` spaceTrainNumber `with the fare` fareId `in search` searchId `by resetting the selection` false }
                .isInstanceOf(NoSuchElementException::class.java)
                .hasMessage("unknown search id $searchId")
    }

    @Test
    fun `not be able to select an unknown space train`(@RoundTrip search: Search) {
        assertThatThrownBy { selectSpaceTrain `having the number` "unknown" `with the fare` randomUUID() `in search` search.id `by resetting the selection` false }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun `not be able to select an unknown fare`(@RoundTrip search: Search) {
        val spaceTrainNumber = search.spaceTrains.first().number
        val fareId = randomUUID()

        assertThatThrownBy { selectSpaceTrain `having the number` spaceTrainNumber `with the fare` fareId `in search` search.id `by resetting the selection` false }
                .isInstanceOf(NoSuchElementException::class.java)
    }
}
