package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.spaceport.OnEarth
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

@DisplayName("Journey should")
class JourneyShould : EqualityShould<Journey> {

    @Test
    fun `not be created if departure and arrival are on the same AstronomicalBody`(@OnEarth departure: SpacePort, @OnEarth arrival: SpacePort) {
        assertThatThrownBy { Journey(departure, now().plusWeeks(1), arrival) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot create a Journey departing and arriving on the same AstronomicalBody")
    }

    @Test
    fun `not be created departure schedule is in the past`(journey: Journey) {
        assertThatThrownBy { journey.copy(departureSchedule = now().minusWeeks(1)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot create a Journey with a departure scheduled in the past")
    }

    @Test
    fun `be connected to the next journey if current arrival space port is the departure of the next journey`(journey: Journey) {
        val nextJourney = journey.copy(departureSpacePort = journey.arrivalSpacePort, arrivalSpacePort = journey.departureSpacePort)
        assertThat(journey `is connected to` nextJourney).isTrue()
    }

    @Test
    fun `not be connected to the next journey if current arrival space port is not the departure of the next journey`(journey: Journey) {
        val nextJourney = journey.copy()
        assertThat(journey `is connected to` nextJourney).isFalse()
    }
}