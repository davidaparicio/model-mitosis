package com.beyondxscratch.mandaloreexpress.domain.api

import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.criteria.Criteria
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import java.util.*

interface SearchForSpaceTrains {
    val searches: Searches
    infix fun satisfying(criteria: Criteria): Search

    infix fun `identified by`(searchId: UUID): Search {
        return (searches `find search identified by` searchId)?: throw NoSuchElementException("Unknown search $searchId")
    }
}