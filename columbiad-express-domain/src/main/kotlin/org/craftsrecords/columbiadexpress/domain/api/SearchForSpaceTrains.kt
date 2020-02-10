package org.craftsrecords.columbiadexpress.domain.api

import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria
import org.craftsrecords.columbiadexpress.domain.spi.Searches

interface SearchForSpaceTrains {
    val searches: Searches
    infix fun satisfying(criteria: Criteria): Search
}