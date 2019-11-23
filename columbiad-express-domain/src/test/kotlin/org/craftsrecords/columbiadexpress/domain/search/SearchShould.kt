package org.craftsrecords.columbiadexpress.domain.search

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.junit.jupiter.api.Test

class SearchShould : EqualityShould<Search> {
    @Test
    fun `not have an empty list of space trains`(criteria: Criteria) {
        assertThatThrownBy { Search(criteria = criteria, spaceTrains = listOf()) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("list of space trains cannot be empty")
    }
}