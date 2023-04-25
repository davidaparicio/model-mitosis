package com.beyondxscratch.mandaloreexpress.domain.search

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.beyondxscratch.mandaloreexpress.domain.EqualityShould
import com.beyondxscratch.mandaloreexpress.domain.search.SpaceTrain.Companion.get
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.search.selection.SelectedSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.selection.Selection
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.INBOUND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.OUTBOUND
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Bound.values
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Fare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.price
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID

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
            mapOf(
                bound to SelectedSpaceTrain(
                    spaceTrainNumber = spaceTrainNumber,
                    fareId = randomUUID(),
                    price = price()
                )
            )
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
            mapOf(
                bound to SelectedSpaceTrain(
                    spaceTrainNumber = spaceTrainNumber,
                    fareId = randomUUID(),
                    price = price()
                )
            )
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
            mapOf(
                wrongBound to SelectedSpaceTrain(
                    spaceTrainNumber = spaceTrainNumber,
                    fareId = fare.id,
                    price = fare.price
                )
            )
        )

        assertThatThrownBy { search.copy(selection = invalidSelection) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("selected space trains don't correspond to the right bound")
    }

    @Test
    fun `have an incomplete selection if no space trains have been selected`(@OneWay baseSearch: Search) {
        val search = baseSearch.copy(selection = Selection())
        assertThat(search.isSelectionComplete()).isFalse
    }

    @Test
    fun `have an complete selection if a space train has been selected for a oneway search`(@OneWay baseSearch: Search) {
        val (search) = baseSearch.selectAnOutboundSpaceTrain()

        assertThat(search.isSelectionComplete()).isTrue
    }

    @Test
    fun `have an incomplete selection if only one space trains have been selected for a round trip`(@RoundTrip baseSearch: Search) {
        val (search) = baseSearch.selectAnOutboundSpaceTrain()

        assertThat(search.isSelectionComplete()).isFalse
    }

    @Test
    fun `have an complete selection if all space trains have been selected for a round trip`(@RoundTrip baseSearch: Search) {
        val (search) =
            baseSearch
                .selectAnOutboundSpaceTrain()
                .selectAnInboundSpaceTrain()

        assertThat(search.isSelectionComplete()).isTrue
    }

    @Test
    fun `not be able to select incompatible space trains`(@RoundTrip search: Search) {
        val (newSearch, incompatibleInbound, inboundFare) = search.selectOutboundAndReturnIncompatibleInbound()

        assertThatThrownBy { newSearch.selectSpaceTrainWithFare(incompatibleInbound.number, inboundFare.id, false) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("cannot select incompatible space trains")
    }

    @Test
    fun `be able to select an incompatible space train on reset selection`(@RoundTrip search: Search) {
        val (searchWithPartialSelection, incompatibleInbound, inboundFare) = search.selectOutboundAndReturnIncompatibleInbound()

        val newSearch =
            searchWithPartialSelection.selectSpaceTrainWithFare(incompatibleInbound.number, inboundFare.id, true)

        assertThat(newSearch.selection).hasSize(1)
        assertThat(newSearch.selection).containsValue(
            SelectedSpaceTrain(
                incompatibleInbound.number,
                inboundFare.id,
                inboundFare.price
            )
        )
    }

    private fun Search.selectOutboundAndReturnIncompatibleInbound(): Triple<Search, SpaceTrain, Fare> {
        val outbound = spaceTrains.first { it.bound == OUTBOUND }
        val outboundFare = outbound.fares.first()
        val incompatibleInbound =
            spaceTrains.first { it.bound == INBOUND && !it.compatibleSpaceTrains.contains(outbound.number) }
        val inboundFare = incompatibleInbound.fares.first()

        val newSearch = selectSpaceTrainWithFare(outbound.number, outboundFare.id, false)
        return Triple(newSearch, incompatibleInbound, inboundFare)
    }

    @Test
    fun `not have any space train compatibility for a one way search`(@OneWay search: Search, spaceTrain: SpaceTrain) {
        val invalidSpaceTrain = spaceTrain.copy(compatibleSpaceTrains = setOf("42"))
        val spaceTrains = search.spaceTrains + invalidSpaceTrain
        assertThatThrownBy { search.copy(spaceTrains = spaceTrains) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SpaceTrains cannot have compatibilities in a one way search")
    }

    @Test
    fun `have space train compatibilities for a round trip search`(@RoundTrip search: Search, spaceTrain: SpaceTrain) {

        val invalidSpaceTrain = spaceTrain.copy(compatibleSpaceTrains = emptySet())
        val spaceTrains = search.spaceTrains + invalidSpaceTrain
        assertThatThrownBy { search.copy(spaceTrains = spaceTrains) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SpaceTrains must have compatibilities in a round trip search")
    }

    @Test
    fun `not have any space train compatible with a space train on the same bound`(
        @RoundTrip search: Search,
        invalidSpaceTrainBase: SpaceTrain
    ) {
        val spaceTrainOnTheSameBound = search.spaceTrains.first { it.bound == invalidSpaceTrainBase.bound }
        val invalidSpaceTrain =
            invalidSpaceTrainBase
                .copy(compatibleSpaceTrains = setOf(spaceTrainOnTheSameBound.number))
        val spaceTrains = search.spaceTrains + invalidSpaceTrain

        assertThatThrownBy { search.copy(spaceTrains = spaceTrains) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("SpaceTrains cannot be compatible with another space train on the same bound")
    }

    @Test
    fun `not have any space train compatible with an unknown one`(
        @RoundTrip search: Search,
        invalidSpaceTrainBase: SpaceTrain
    ) {
        val invalidSpaceTrain =
            invalidSpaceTrainBase
                .copy(compatibleSpaceTrains = setOf("unknown"))
        val spaceTrains = search.spaceTrains + invalidSpaceTrain

        assertThatThrownBy { search.copy(spaceTrains = spaceTrains) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("some SpaceTrains have unknown compatibilities")
    }

    @Test
    fun `only have symmetric space train compatibilities`(
        @RoundTrip search: Search,
        @Inbound invalidSpaceTrainBase: SpaceTrain
    ) {

        val outbound = search.spaceTrains.first { it.bound == OUTBOUND }
        val invalidSpaceTrain =
            invalidSpaceTrainBase
                .copy(compatibleSpaceTrains = setOf(outbound.number))
        val spaceTrains = search.spaceTrains + invalidSpaceTrain

        assertThatThrownBy { search.copy(spaceTrains = spaceTrains) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("some SpaceTrains don't respect a symmetric compatibility")
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

    @Test
    fun `return all the space trains of a bound when asking for the selectable ones and selection is empty`(@RoundTrip baseSearch: Search) {
        val search = baseSearch.copy(selection = Selection())
        val allInbounds = search.spaceTrains[INBOUND]
        assertThat(search.selectableSpaceTrains(INBOUND)).isEqualTo(allInbounds)
    }

    @Test
    fun `return only selectable space trains of a bound when selection is not empty`(@RoundTrip baseSearch: Search) {
        val (search, outbound, _) = baseSearch.copy(selection = Selection()).selectAnOutboundSpaceTrain()
        val (searchWithFullSelection, _, _) = search.selectAnInboundSpaceTrain()

        assertThat(searchWithFullSelection.selectableSpaceTrains(INBOUND))
            .isNotEmpty
            .allSatisfy {
                assertThat(outbound.compatibleSpaceTrains).contains(it.number)
            }
    }

}