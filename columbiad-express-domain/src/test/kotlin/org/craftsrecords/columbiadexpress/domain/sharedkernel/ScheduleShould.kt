package org.craftsrecords.columbiadexpress.domain.sharedkernel

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime.now

class ScheduleShould : EqualityShould<Schedule> {
    @Test
    fun `not have a departure in the past`() {
        val departureInThePast = now().minusDays(2)
        val arrival = now().plusWeeks(1)

        assertThatThrownBy { Schedule(departureInThePast, arrival) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("departure cannot be in the past")
    }

    @Test
    fun `not arrive before its departure schedule`() {
        val departure = now().plusDays(2)
        val arrival = departure.minusHours(1)

        assertThatThrownBy { Schedule(departure, arrival) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("arrival cannot precede the departure")
    }

    @Test
    fun `compute its duration`() {
        val departure = now().plusDays(1)
        val schedule = Schedule(departure, departure.plusDays(4).plusHours(7))

        assertThat(schedule.duration).isEqualTo(Duration.ofHours(103))
    }

}