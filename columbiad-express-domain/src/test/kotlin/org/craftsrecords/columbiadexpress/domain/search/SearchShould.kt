package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.*
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.search.Bound.*
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.Journey
import org.craftsrecords.columbiadexpress.domain.search.selection.SelectedSpaceTrain
import org.craftsrecords.columbiadexpress.domain.search.selection.Selection
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

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

    @Test
    fun `only have selected space trains existing inside the result list`(@RoundTrip search: Search) {
        val invalidSelection = Selection(
                mapOf(OUTBOUND to SelectedSpaceTrain(spaceTrainNumber = "unknown", fareId = randomUUID(), price = price()))
        )

        assertThatThrownBy { search.copy(selection = invalidSelection) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("unknown space train in the selection")
    }

    @Test
    fun `only have selected fares existing inside the result list`(@RoundTrip search: Search) {
        val spaceTrain = search.spaceTrains.first()
        val spaceTrainNumber = spaceTrain.number
        val bound = spaceTrain.bound

        val invalidSelection = Selection(
                mapOf(bound to SelectedSpaceTrain(spaceTrainNumber = spaceTrainNumber, fareId = randomUUID(), price = price()))
        )

        assertThatThrownBy { search.copy(selection = invalidSelection) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("unknown fare in the selection")
    }

    @Test
    fun `only have selected fares with the right price`(@RoundTrip search: Search) {
        val spaceTrain = search.spaceTrains.first()
        val spaceTrainNumber = spaceTrain.number
        val bound = spaceTrain.bound

        val invalidSelection = Selection(
                mapOf(bound to SelectedSpaceTrain(spaceTrainNumber = spaceTrainNumber, fareId = randomUUID(), price = price()))
        )

        assertThatThrownBy { search.copy(selection = invalidSelection) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("unknown fare in the selection")
    }

    @Test
    fun `only have selected space trains corresponding to the right bound`(@RoundTrip search: Search) {
        val spaceTrain = search.spaceTrains.first()
        val spaceTrainNumber = spaceTrain.number
        val fare = spaceTrain.fares.first()
        val wrongBound = values().first { it != spaceTrain.bound }

        val invalidSelection = Selection(
                mapOf(wrongBound to SelectedSpaceTrain(spaceTrainNumber = spaceTrainNumber, fareId = fare.id, price = fare.price))
        )

        assertThatThrownBy { search.copy(selection = invalidSelection) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("selected space trains don't correspond to the right bound")
    }

    @Test
    fun `have an incomplete selection if no space trains have been selected`(@OneWay baseSearch: Search) {
        val search = baseSearch.copy(selection = Selection())
        assertThat(search.isSelectionComplete()).isFalse()
    }

    @Test
    fun `have an complete selection if a space train has been selected for a oneway search`(@OneWay baseSearch: Search) {
        val search = baseSearch.selectOneOutboundSpaceTrain()

        assertThat(search.isSelectionComplete()).isTrue()
    }

    @Test
    fun `have an incomplete selection if only one space trains have been selected for a round trip`(@RoundTrip baseSearch: Search) {
        val search = baseSearch.selectOneOutboundSpaceTrain()

        assertThat(search.isSelectionComplete()).isFalse()
    }

    @Test
    fun `have an complete selection if all space trains have been selected for a round trip`(@RoundTrip baseSearch: Search) {
        val spaceTrain = baseSearch.spaceTrains.first { it.bound == INBOUND }
        val fare = spaceTrain.fares.first()

        val search = baseSearch
                .selectOneOutboundSpaceTrain()
                .selectSpaceTrainWithFare(spaceTrain.number, fare.id)

        assertThat(search.isSelectionComplete()).isTrue()
    }

    private fun Search.selectOneOutboundSpaceTrain(): Search {
        val spaceTrain = spaceTrains.first { it.bound == OUTBOUND }
        val fare = spaceTrain.fares.first()
        return this
                .copy(selection = Selection())
                .selectSpaceTrainWithFare(spaceTrain.number, fare.id)
    }
}