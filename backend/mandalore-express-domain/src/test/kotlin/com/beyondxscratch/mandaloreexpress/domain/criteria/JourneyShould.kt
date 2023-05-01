package com.beyondxscratch.mandaloreexpress.domain.criteria

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class JourneyShould : EqualityShould<Journey> {

    @Test
    fun `not be created departure schedule is in the past`(journey: Journey) {
        assertThatThrownBy { journey.copy(departureSchedule = now().minusWeeks(1)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Cannot create a Journey with a departure scheduled in the past")
    }

    @Test
    fun `be connected to the next journey if current arrival space port is the departure of the next journey`(journey: Journey) {
        val nextJourney = journey.copy(
            departureSpacePortId = journey.arrivalSpacePortId,
            arrivalSpacePortId = journey.departureSpacePortId
        )
        assertThat(journey `is connected to` nextJourney).isTrue
    }

    @Test
    fun `not be connected to the next journey if current arrival space port is not the departure of the next journey`(
        journey: Journey
    ) {
        val nextJourney = journey.copy()
        assertThat(journey `is connected to` nextJourney).isFalse
    }
}