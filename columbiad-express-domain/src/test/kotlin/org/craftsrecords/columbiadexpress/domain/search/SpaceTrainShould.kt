package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class SpaceTrainShould() : EqualityShould<SpaceTrain> {

    @Test
    fun `not have a departure schedule in the past`(fare: Fare) {
        assertThatThrownBy { SpaceTrain(now().minusDays(2), now().plusWeeks(1), setOf(fare)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("departureSchedule cannot be in the past")
    }

    @Test
    fun `not arrive before its departure schedule`(fare: Fare) {
        assertThatThrownBy { SpaceTrain(now().plusDays(1), now().minusDays(1), setOf(fare)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("arrivalSchedule cannot precede the departureSchedule")
    }

    @Test
    fun `have at least one fare`() {
        assertThatThrownBy { SpaceTrain(now().plusDays(1), now().plusWeeks(1), setOf()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("SpaceTrain must have at least one fare")
    }
}