package com.beyondxscratch.mandaloreexpress.domain

import com.beyondxscratch.mandaloreexpress.domain.api.BookSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.BookSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.mandaloreexpress.domain.api.RetrieveSpacePortsShould
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.mandaloreexpress.domain.api.SearchForSpaceTrainsShould
import com.beyondxscratch.mandaloreexpress.domain.api.SelectSpaceTrain
import com.beyondxscratch.mandaloreexpress.domain.api.SelectSpaceTrainShould
import com.beyondxscratch.mandaloreexpress.domain.search.RoundTrip
import com.beyondxscratch.mandaloreexpress.domain.search.Search
import com.beyondxscratch.mandaloreexpress.domain.stubs.InMemoryBookings
import com.beyondxscratch.mandaloreexpress.domain.stubs.InMemorySearches
import com.beyondxscratch.mandaloreexpress.domain.stubs.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class MandaloreExpressShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould,
    SelectSpaceTrainShould, BookSpaceTrainsShould {

    private val columbiadExpress =
        MandaloreExpress(
            InMemorySpacePorts(),
            InMemorySearches(),
            InMemoryBookings()
        )
    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = columbiadExpress

    override val searchForSpaceTrains: SearchForSpaceTrains
        get() = columbiadExpress

    override val selectSpaceTrain: SelectSpaceTrain
        get() = columbiadExpress

    override val bookSpaceTrains: BookSpaceTrains
        get() = columbiadExpress

    @BeforeEach
    fun setUp() {
        columbiadExpress.searches.save(search)
    }
}