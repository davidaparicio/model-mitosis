package com.beyondxscratch.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import com.beyondxscratch.columbiadexpress.domain.Random
import com.beyondxscratch.columbiadexpress.domain.search.RoundTrip
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Criteria
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Journey
import com.beyondxscratch.columbiadexpress.domain.spaceport.OnEarth
import com.beyondxscratch.columbiadexpress.domain.spaceport.SpacePort
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.now

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
    fun `throw an error if departure and arrival are on the same AstronomicalBody`(@OnEarth departure: SpacePort, @Random @OnEarth arrival: SpacePort) {
        val criteria = Criteria(listOf(Journey(departure.id, now().plusDays(2), arrival.id)))
        assertThatThrownBy { searchForSpaceTrains satisfying criteria }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Cannot perform a trip departing and arriving on the same AstronomicalBody")
    }

}