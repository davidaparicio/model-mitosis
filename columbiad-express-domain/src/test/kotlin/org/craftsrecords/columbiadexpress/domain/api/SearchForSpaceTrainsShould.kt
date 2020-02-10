package org.craftsrecords.columbiadexpress.domain.api

import org.assertj.core.api.Assertions.assertThat
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.junit.jupiter.api.Test

interface SearchForSpaceTrainsShould {

    val searchForSpaceTrains: SearchForSpaceTrains

    @Test
    fun `find space trains related to search criteria`(@RoundTrip criteria: Criteria) {
        val search = searchForSpaceTrains satisfying criteria

        assertThat(search.spaceTrains).isNotEmpty
        assertThat(search.criteria).isEqualTo(criteria)
        assertThat(searchForSpaceTrains.searches `find search identified by` search.id).isEqualTo(search)
    }

}