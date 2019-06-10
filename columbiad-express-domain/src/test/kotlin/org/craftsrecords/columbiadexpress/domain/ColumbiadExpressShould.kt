package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.api.RetrieveSpacePortsShould
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.stubs.InMemorySpacePorts
import org.junit.jupiter.api.DisplayName

@DisplayName("ColumbiadExpress should")
class ColumbiadExpressShould : RetrieveSpacePortsShould {

    override val retrieveSpacePorts: RetrieveSpacePorts
        get() = ColumbiadExpress(InMemorySpacePorts())
}