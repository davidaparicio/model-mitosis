package org.craftsrecords.columbiadexpress.domain.search.criteria

import org.craftsrecords.columbiadexpress.domain.EqualityShould

class CriteriaShould(private val journey: Journey) : EqualityShould<Criteria> {

    /*
    @Test
    fun `create Criteria`(@OnEarth spacePortOnEarth: SpacePort, @OnMoon spacePortOnMoon: SpacePort) {
        val journeys =
                listOf(
                        journey,
                        connectionTo(journey) departingAt journey.departureSchedule.plusWeeks(1))

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
                        connectionTo(journey) departingAt now().plusDays(1))

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
    }*/
}