package com.beyondxscratch.columbiadexpress.domain.api

import com.beyondxscratch.columbiadexpress.domain.search.Search
import com.beyondxscratch.columbiadexpress.domain.search.criteria.Criteria
import com.beyondxscratch.columbiadexpress.domain.spi.Searches

interface SearchForSpaceTrains {
    val searches: Searches
    infix fun satisfying(criteria: Criteria): Search
}