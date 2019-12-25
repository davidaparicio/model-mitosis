package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePortsShould
import org.craftsrecords.columbiadexpress.domain.api.SearchForSpaceTrainsShould
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.api.SearchForSpaceTrains
import org.craftsrecords.columbiadexpress.domain.spaceport.stubs.InMemorySearches
import org.craftsrecords.columbiadexpress.domain.spaceport.stubs.InMemorySpacePorts

class ColumbiadExpressShould : RetrieveSpacePortsShould, SearchForSpaceTrainsShould {

    private val columbiadExpress =
            ColumbiadExpress(InMemorySpacePorts(), InMemorySearches())
    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = columbiadExpress

    override val searchForSpaceTrains: SearchForSpaceTrains
        get() = columbiadExpress
}