package com.beyondxscratch.mandaloreexpress.domain.search.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.OneWay
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.SpaceTrain.Companion.get
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.spaceTrainsFrom
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class SpaceTrainShould(private val spaceTrain: SpaceTrain) : EqualityShould<SpaceTrain> {

    @Test
    fun `have at least one fare`() {
        assertThatThrownBy { spaceTrain.copy(fares = setOf()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("SpaceTrain must have at least one fare")
    }

    @Test
    fun `not be compatible with itself`() {
        assertThatThrownBy { spaceTrain.copy(compatibleSpaceTrains = setOf(spaceTrain.number)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("SpaceTrain cannot be compatible with itself")
    }

    @Test
    fun `return all the space trains of a given bound`(@RoundTrip criteria: Criteria) {
        val spaceTrains = spaceTrainsFrom(criteria)
        val allInbounds = spaceTrains.filter { it.bound == INBOUND }
        assertThat(spaceTrains[INBOUND]).isEqualTo(allInbounds)
    }


    @Test
    fun `return no inbound space trains in case of a one way`(@OneWay criteria: Criteria) {
        val spaceTrains = spaceTrainsFrom(criteria)
        assertThat(spaceTrains[INBOUND]).isEmpty()
    }

    @Test
    fun `return the fare corresponding to a given id if present`() {
        val fare = spaceTrain.fares.first()
        assertThat(spaceTrain.getFare(fare.id)).isEqualTo(fare)
    }

    @Test
    fun `return NoSuchElementException for a fare id not belonging to the space train`() {
        assertThatCode { spaceTrain.getFare(UUID.randomUUID()) }.isInstanceOf(NoSuchElementException::class.java)
    }
}
