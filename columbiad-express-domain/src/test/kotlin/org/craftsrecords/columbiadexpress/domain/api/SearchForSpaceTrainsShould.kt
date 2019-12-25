package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.search.criteria.RoundTrip
import org.craftsrecords.columbiadexpress.domain.spaceport.api.SearchForSpaceTrains
import org.junit.jupiter.api.Test

interface SearchForSpaceTrainsShould {

    val searchForSpaceTrains: SearchForSpaceTrains

    @Test
    fun `find space trains related to search criteria`(@RoundTrip criteria: Criteria) {
        val search = searchForSpaceTrains satisfying criteria
        assertThat(search.spaceTrains).isNotEmpty
    }

}