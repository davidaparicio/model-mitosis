package org.craftsrecords.columbiadexpress.domain.sharedkernel

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.sharedkernel.Bound.INBOUND
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class BoundShould {

    @Test
    fun `return the corresponding bound from a journey index`() {
        assertThat(Bound.fromJourneyIndex(1)).isEqualTo(INBOUND)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 4])
    fun `throw an exception when the journey index is not valid`(journeyIndex: Int) {
        assertThatThrownBy { Bound.fromJourneyIndex(journeyIndex) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Journey index $journeyIndex is outside the supported range [0,1]")
    }

    @ParameterizedTest
    @CsvSource("OUTBOUND,INBOUND", "INBOUND,OUTBOUND")
    fun `return the right opposite way`(way: Bound, opposite: Bound) {
        assertThat(way.oppositeWay()).isEqualTo(opposite)
    }

}