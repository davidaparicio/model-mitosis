package com.beyondxscratch.mandaloreexpress.domain.criteria

import com.beyondxscratch.mandaloreexpress.domain.Criteria
import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.Journey
import com.beyondxscratch.mandaloreexpress.domain.spaceport.OnCoruscant
import com.beyondxscratch.mandaloreexpress.domain.spaceport.OnMandalore
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

class CriteriaShould(private val journey: Journey) : EqualityShould<Criteria> {


    @Test
    fun `create Criteria`() {
        val journeys =
            listOf(
                journey,
                inboundOf(journey) departingAt journey.departureSchedule.plusWeeks(1)
            )

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
                inboundOf(journey) departingAt now().plusDays(1)
            )

        assertThatThrownBy { Criteria(journeys) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Criteria must only have journeys ordered by departure schedule")
    }

    @Test
    fun `have only connected journeys`(
        @OnCoruscant spacePortOnCoruscant: SpacePort,
        @OnMandalore spacePortOnMoon: SpacePort
    ) {
        val journeys =
            listOf(
                Journey(spacePortOnCoruscant.id, now().plusDays(1), spacePortOnMoon.id),
                Journey(spacePortOnCoruscant.id, now().plusWeeks(1), spacePortOnMoon.id)
            )
        assertThatThrownBy { Criteria(journeys) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Criteria must only have connected journeys")
    }

    @Test
    fun `have at least 5 days between all journeys`(journey: Journey) {
        val tooCloseJourney = inboundOf(journey).copy(departureSchedule = journey.departureSchedule)

        assertThatThrownBy { Criteria(listOf(journey, tooCloseJourney)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("An elapse time of 5 days must be respected between journeys")
    }
}