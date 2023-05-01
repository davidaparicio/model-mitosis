package com.beyondxscratch.mandaloreexpress.domain.stubs

import com.beyondxscratch.mandaloreexpress.domain.Search
import com.beyondxscratch.mandaloreexpress.domain.spi.Searches
import java.util.UUID

@Stub
class InMemorySearches : Searches {

    val searches: MutableMap<UUID, Search> = hashMapOf()

    override fun `find search identified by`(searchId: UUID): Search? = searches[searchId]
    override fun save(search: Search): Search {
        searches[search.id] = search
        return search
    }
}