package org.craftsrecords.columbiadexpress.domain.spaceport.api

import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.search.criteria.Criteria

interface SearchForSpaceTrains {
    infix fun satisfying(criteria: Criteria): Search
}