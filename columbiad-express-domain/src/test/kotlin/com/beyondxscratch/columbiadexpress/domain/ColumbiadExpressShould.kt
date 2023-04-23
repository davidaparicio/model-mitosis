package com.beyondxscratch.columbiadexpress.domain

import com.beyondxscratch.columbiadexpress.domain.api.BookSpaceTrains
import com.beyondxscratch.columbiadexpress.domain.api.BookSpaceTrainsShould
import com.beyondxscratch.columbiadexpress.domain.api.RetrieveSpacePorts
import com.beyondxscratch.columbiadexpress.domain.api.RetrieveSpacePortsShould
import com.beyondxscratch.columbiadexpress.domain.api.SearchForSpaceTrains
import com.beyondxscratch.columbiadexpress.domain.api.SearchForSpaceTrainsShould
import com.beyondxscratch.columbiadexpress.domain.api.SelectSpaceTrain
import com.beyondxscratch.columbiadexpress.domain.api.SelectSpaceTrainShould
import com.beyondxscratch.columbiadexpress.domain.search.RoundTrip
import com.beyondxscratch.columbiadexpress.domain.search.Search
import com.beyondxscratch.columbiadexpress.domain.stubs.InMemoryBookings
import com.beyondxscratch.columbiadexpress.domain.stubs.InMemorySearches
import com.beyondxscratch.columbiadexpress.domain.stubs.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class ColumbiadExpressShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould,
    SelectSpaceTrainShould, BookSpaceTrainsShould {

    private val columbiadExpress =
        com.beyondxscratch.columbiadexpress.domain.ColumbiadExpress(
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