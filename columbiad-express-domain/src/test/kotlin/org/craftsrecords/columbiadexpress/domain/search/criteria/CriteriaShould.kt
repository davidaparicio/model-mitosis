package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.InjectDomainObjects
import org.craftsrecords.columbiadexpress.domain.spaceport.OnEarth
import org.craftsrecords.columbiadexpress.domain.spaceport.OnMoon
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

@InjectDomainObjects
class CriteriaShould(private val journey: Journey) {

    @Test
    fun `create Criteria`(@OnEarth spacePortOnEarth: SpacePort, @OnMoon spacePortOnMoon: SpacePort) {
        val journeys =
                listOf(
                        journey,
                        createConnectionTo(journey) departingAt journey.departureSchedule.plusWeeks(1))

        assertThatCode { Criteria(journeys) }.doesNotThrowAnyException()
    }

    @Test
    fun `contain at least one journey`() {
        assertThatThrownBy { Criteria(emptyList()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Criteria must contain at least one journey")
    }

    @Test
    fun `have only journeys ordered by departureSchedule`() {
        val journeys =
                listOf(
                        journey.copy(departureSchedule = now().plusWeeks(1)),
                        createConnectionTo(journey) departingAt now().plusDays(1))

        assertThatThrownBy { Criteria(journeys) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Criteria must only have journeys ordered by departure schedule")
    }


    @Test
    fun `have only connected journeys`(@OnEarth spacePortOnEarth: SpacePort, @OnMoon spacePortOnMoon: SpacePort) {
        val journeys =
                listOf(
                        Journey(spacePortOnEarth, now().plusDays(1), spacePortOnMoon),
                        Journey(spacePortOnEarth, now().plusWeeks(1), spacePortOnMoon))
        assertThatThrownBy { Criteria(journeys) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Criteria must only have connected journeys")
    }
}