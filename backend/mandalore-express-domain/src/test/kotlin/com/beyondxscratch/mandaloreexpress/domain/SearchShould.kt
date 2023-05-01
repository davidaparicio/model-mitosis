package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Inbound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.Outbound
import com.beyondxscratch.mandaloreexpress.domain.spacetrain.SpaceTrain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class SearchShould : EqualityShould<Search> {
    @Test
    fun `have no space trains if criteria cannot be satisfied`(criteria: Criteria) {
        assertThatCode { Search(criteria = criteria, spaceTrains = listOf()) }.doesNotThrowAnyException()
    }

    @Test
    fun `not have a space train which doesn't correspond to a criteria journey`(
        journey: Journey,
        spaceTrain: SpaceTrain
    ) {
        val criteria = Criteria(listOf(journey))
        val invalidSpaceTrain =
            spaceTrain.copy(originId = journey.arrivalSpacePortId, destinationId = journey.departureSpacePortId)

        assertThatThrownBy { Search(criteria = criteria, spaceTrains = listOf(invalidSpaceTrain)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("some space trains don't correspond to any journey from the criteria")
    }

    @Test
    fun `have a space train for each bound`(
        @Outbound outbound: Pair<Journey, SpaceTrain>,
        @Inbound inboundJourney: Journey
    ) {
        val (outboundJourney, outboundSpaceTrain) = outbound
        val criteria = Criteria(listOf(outboundJourney, inboundJourney))

        assertThatThrownBy { Search(criteria = criteria, spaceTrains = listOf(outboundSpaceTrain)) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("some journeys don't have at least one corresponding space train")
    }

    @Test
    fun `find the space train of a given number`(@OneWay baseSearch: Search, spaceTrain: SpaceTrain) {
        val wantedNumber = "5435345"
        val wantedSpaceTrain = spaceTrain.copy(number = wantedNumber)
        val search = baseSearch.copy(spaceTrains = baseSearch.spaceTrains + wantedSpaceTrain)
        assertThat(search.getSpaceTrainWithNumber(wantedNumber)).isEqualTo(wantedSpaceTrain)
    }

    @Test
    fun `not be able to find a space train of an unknown number`(@OneWay search: Search) {
        assertThatThrownBy { search.getSpaceTrainWithNumber("unknown") }
            .isInstanceOf(NoSuchElementException::class.java)
    }

}