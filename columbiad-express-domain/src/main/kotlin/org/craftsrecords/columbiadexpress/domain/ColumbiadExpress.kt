package org.craftsrecords.columbiadexpress.domain

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.api.RetrieveSpacePorts
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts

class ColumbiadExpress(private val spacePorts: SpacePorts) : RetrieveSpacePorts {

    override fun `having their name containing`(partialName: String): Set<SpacePort> {
        return spacePorts.getAllSpacePorts()
                .filter { it.name.contains(partialName) }
                .toSet()
    }
}