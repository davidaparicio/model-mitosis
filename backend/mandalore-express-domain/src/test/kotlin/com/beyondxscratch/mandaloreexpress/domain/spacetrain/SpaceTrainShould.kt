package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.OneWay
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain.Companion.get
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SpaceTrainShould : EqualityShould<SpaceTrain> {

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
}
