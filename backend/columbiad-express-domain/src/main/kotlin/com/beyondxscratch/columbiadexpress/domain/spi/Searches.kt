package com.beyondxscratch.columbiadexpress.domain.spi

import com.beyondxscratch.columbiadexpress.domain.search.Search
import java.util.UUID

interface Searches {
    infix fun `find search identified by`(searchId: UUID): Search?
    infix fun save(search: Search): Search
}