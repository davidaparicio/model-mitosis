package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.booking.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.BookSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.search.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePortsShould
import com.beyondxscratch.mandaloreexpress.domain.search.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.search.api.SelectSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.api.SelectSpaceTrainShould
import com.beyondxscratch.mandaloreexpress.domain.search.SpaceTrainsFinder
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.booking.spi.InMemoryBookings
import com.beyondxscratch.mandaloreexpress.domain.search.spi.InMemorySearches
import com.beyondxscratch.mandaloreexpress.domain.search.spi.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class MandaloreExpressShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould,
    SelectSpaceTrainShould, BookSpaceTrainsShould {

    private val mandaloreExpress =
        SpaceTrainsFinder(
            InMemorySpacePorts(),
            InMemorySearches(),
            InMemoryBookings()
        )
    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = mandaloreExpress

    override val searchForSpaceTrains: SearchForSpaceTrains
        get() = mandaloreExpress

    override val selectSpaceTrain: SelectSpaceTrain
        get() = mandaloreExpress

    override val bookSpaceTrains: BookSpaceTrains
        get() = mandaloreExpress

    @BeforeEach
    fun setUp() {
        mandaloreExpress.searches.save(search)
    }
}