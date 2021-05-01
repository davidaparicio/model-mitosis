package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.api.BookSpaceTrains
import org.craftsrecords.columbiadexpress.domain.api.BookSpaceTrainsShould
import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePortsShould
import org.craftsrecords.columbiadexpress.domain.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.domain.api.SearchForSpaceTrainsShould
import org.craftsrecords.columbiadexpress.domain.api.SelectSpaceTrain
import org.craftsrecords.columbiadexpress.domain.api.SelectSpaceTrainShould
import org.craftsrecords.columbiadexpress.domain.search.RoundTrip
import org.craftsrecords.columbiadexpress.domain.search.Search
import org.craftsrecords.columbiadexpress.domain.stubs.InMemoryBookings
import org.craftsrecords.columbiadexpress.domain.stubs.InMemorySearches
import org.craftsrecords.columbiadexpress.domain.stubs.InMemorySpacePorts
import org.junit.jupiter.api.BeforeEach

class ColumbiadExpressShould(@RoundTrip val search: Search) : RetrieveSpacePortsShould, SearchForSpaceTrainsShould,
    SelectSpaceTrainShould, BookSpaceTrainsShould {

    private val columbiadExpress =
        ColumbiadExpress(InMemorySpacePorts(), InMemorySearches(), InMemoryBookings())
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