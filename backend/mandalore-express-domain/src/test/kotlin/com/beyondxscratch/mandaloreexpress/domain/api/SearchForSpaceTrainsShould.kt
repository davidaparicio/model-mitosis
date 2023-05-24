package com.beyondxscratch.mandaloreexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.beyondxscratch.mandaloreexpress.domain.Random
import com.beyondxscratch.mandaloreexpress.domain.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Journey
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.spaceport.OnCoruscant
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now
import java.util.*
import java.util.UUID.randomUUID

interface SearchForSpaceTrainsShould {

    val searchForSpaceTrains: SearchForSpaceTrains

    @Test
    fun `find space trains related to search criteria`(@RoundTrip criteria: Criteria) {
        val search = searchForSpaceTrains satisfying criteria

        assertThat(search.spaceTrains).isNotEmpty
        assertThat(search.criteria).isEqualTo(criteria)
        assertThat(searchForSpaceTrains.searches `find search identified by` search.id).isEqualTo(search)
    }

    @Test
    fun `throw an error if departure and arrival are on the same Planet`(@OnCoruscant departure: SpacePort, @Random @OnCoruscant arrival: SpacePort) {
        val criteria = Criteria(listOf(Journey(departure.id, now().plusDays(2), arrival.id)))
        assertThatThrownBy { searchForSpaceTrains satisfying criteria }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot perform a trip departing and arriving on the same Planet")
    }

    @Test
    fun `find the search with the given id`(@RoundTrip searchSaved: Search) {
        val searchFound = searchForSpaceTrains `identified by` searchSaved.id
        assertThat(searchFound).isEqualTo(searchSaved)
    }

    @Test
    fun `throw NoSuchElementException for an unknown search Id`() {
        val unknownId = randomUUID()

        assertThatThrownBy { searchForSpaceTrains `identified by` unknownId }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Unknown search $unknownId")
    }
}