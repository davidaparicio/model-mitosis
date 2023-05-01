package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePortsShould
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.stubs.InMemorySearches
import com.beyondxscratch.mandaloreexpress.domain.stubs.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class MandaloreExpressShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould {

    private val mandaloreExpress =
        MandaloreExpress(
            InMemorySpacePorts(),
            InMemorySearches()
        )
    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = mandaloreExpress

    override val searchForSpaceTrains: SearchForSpaceTrains
        get() = mandaloreExpress


    @BeforeEach
    fun setUp() {
        mandaloreExpress.searches.save(search)
    }
}