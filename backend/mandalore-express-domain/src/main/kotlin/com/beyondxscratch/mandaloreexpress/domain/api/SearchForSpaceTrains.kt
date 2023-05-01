package com.beyondxscratch.mandaloreexpress.domain.api

import com.beyondxscratch.mandaloreexpress.domain.Criteria
import com.beyondxscratch.mandaloreexpress.domain.Search
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches

interface SearchForSpaceTrains {
    val searches: Searches
    infix fun satisfying(criteria: Criteria): Search
}