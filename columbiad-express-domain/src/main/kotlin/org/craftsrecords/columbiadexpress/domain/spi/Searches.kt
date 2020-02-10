package org.craftsrecords.columbiadexpress.domain.spi

import org.craftsrecords.columbiadexpress.domain.search.Search
import java.util.UUID

interface Searches {
    infix fun `find search identified by`(searchId: UUID): Search?
    infix fun save(search: Search): Search
}