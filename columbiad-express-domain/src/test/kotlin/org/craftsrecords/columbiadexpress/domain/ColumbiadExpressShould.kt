package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePortsShould
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.stubs.InMemorySpacePorts

class ColumbiadExpressShould : RetrieveSpacePortsShould {

    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = ColumbiadExpress(InMemorySpacePorts())
}