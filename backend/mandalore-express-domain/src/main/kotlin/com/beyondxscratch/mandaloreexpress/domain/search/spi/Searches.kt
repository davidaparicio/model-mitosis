package com.beyondxscratch.mandaloreexpress.domain.search.spi

import com.beyondxscratch.mandaloreexpress.domain.Search
import java.util.UUID

interface Searches {
    infix fun `find search identified by`(searchId: UUID): Search?
    infix fun save(search: Search): Search
}