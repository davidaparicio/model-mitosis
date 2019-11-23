package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime.now

class SpaceTrainShould(private val spaceTrain: SpaceTrain) : EqualityShould<SpaceTrain> {

    @Test
    fun `not have a departure schedule in the past`() {
        assertThatThrownBy { spaceTrain.copy(departureSchedule = now().minusDays(2)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("departureSchedule cannot be in the past")
    }

    @Test
    fun `not arrive before its departure schedule`() {
        assertThatThrownBy { spaceTrain.copy(arrivalSchedule = spaceTrain.departureSchedule.minusHours(1)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("arrivalSchedule cannot precede the departureSchedule")
    }

    @Test
    fun `have at least one fare`() {
        assertThatThrownBy { spaceTrain.copy(fares = setOf()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("SpaceTrain must have at least one fare")
    }

    @Test
    fun `compute its duration`() {
        val departureSchedule = now().plusDays(1)
        val newSpaceTrain =
                spaceTrain
                        .copy(departureSchedule = departureSchedule,
                                arrivalSchedule = departureSchedule.plusDays(4).plusHours(7))

        assertThat(newSpaceTrain.duration).isEqualTo(Duration.ofHours(103))
    }
}