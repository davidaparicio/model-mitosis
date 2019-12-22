package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import org.junit.jupiter.api.Test

class SearchShould : EqualityShould<Search> {
    @Test
    fun `have no space trains if criteria cannot be satisfied`(criteria: Criteria) {
        assertThatCode { Search(criteria = criteria, spaceTrains = listOf()) }.doesNotThrowAnyException()
    }

    @Test
    fun `not have a space train which doesn't correspond to a criteria journey`(journey: Journey, spaceTrain: SpaceTrain) {
        val criteria = Criteria(listOf(journey))
        val invalidSpaceTrain = spaceTrain.copy(origin = journey.arrivalSpacePort, destination = journey.departureSpacePort)

        assertThatThrownBy { Search(criteria = criteria, spaceTrains = listOf(invalidSpaceTrain)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("some space trains don't correspond to any journey from the criteria")
    }

    @Test
    fun `have a space train for each bound`(@Outbound outbound: Pair<Journey, SpaceTrain>, @Inbound inboundJourney: Journey) {
        val (outboundJourney, outboundSpaceTrain) = outbound
        val criteria = Criteria(listOf(outboundJourney, inboundJourney))

        assertThatThrownBy { Search(criteria = criteria, spaceTrains = listOf(outboundSpaceTrain)) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("some journeys don't have at least one corresponding space train")
    }

}