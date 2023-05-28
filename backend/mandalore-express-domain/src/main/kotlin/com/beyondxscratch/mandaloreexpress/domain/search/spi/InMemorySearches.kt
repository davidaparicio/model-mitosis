package com.beyondxscratch.mandaloreexpress.domain.search.spi

import com.beyondxscratch.mandaloreexpress.annotations.Stub
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.search.spi.Searches
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