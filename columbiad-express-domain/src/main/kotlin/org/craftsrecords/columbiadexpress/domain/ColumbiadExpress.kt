package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.api.DomainService
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts
import java.util.*

@DomainService
class ColumbiadExpress(private val spacePorts: SpacePorts) : RetrieveSpacePorts {
    override fun `identified by`(id: UUID): SpacePort {
        return spacePorts.getAllSpacePorts().first { it.id == id }
    }

    override fun `having in their name`(partialName: String): Set<SpacePort> {
        return spacePorts.getAllSpacePorts()
                .filter { it `has a name containing` partialName }
                .toSet()
    }
}