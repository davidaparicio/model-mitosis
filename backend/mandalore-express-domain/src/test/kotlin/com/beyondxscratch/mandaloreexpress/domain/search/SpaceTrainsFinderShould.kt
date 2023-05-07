package com.beyondxscratch.mandaloreexpress.domain.search

import com.beyondxscratch.mandaloreexpress.domain.search.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.search.api.RetrieveSpacePortsShould
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.search.api.SelectSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.search.api.SelectSpaceTrainShould
import com.beyondxscratch.mandaloreexpress.domain.search.stubs.InMemorySearches
import com.beyondxscratch.mandaloreexpress.domain.search.stubs.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class SpaceTrainsFinderShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould,
    SelectSpaceTrainShould {

    private val mandaloreExpress =
        SpaceTrainsFinder(
            InMemorySpacePorts(),
            InMemorySearches(),
        )
    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = mandaloreExpress

    override val searchForSpaceTrains: SearchForSpaceTrains
        get() = mandaloreExpress

    override val selectSpaceTrain: SelectSpaceTrain
        get() = mandaloreExpress

    @BeforeEach
    fun setUp() {
        mandaloreExpress.searches.save(search)
    }
}