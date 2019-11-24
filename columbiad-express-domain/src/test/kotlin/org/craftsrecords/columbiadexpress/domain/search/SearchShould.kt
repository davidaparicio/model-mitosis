package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThatCode
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.junit.jupiter.api.Test

class SearchShould : EqualityShould<Search> {
    @Test
    fun `have no space trains if criteria cannot be satisfied`(criteria: Criteria) {
        assertThatCode { Search(criteria = criteria, spaceTrains = listOf()) }.doesNotThrowAnyException()
    }
}