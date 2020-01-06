package org.craftsrecords.columbiadexpress.domain.spaceport.api

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts


interface RetrieveSpacePorts {
    val spacePorts: SpacePorts
    infix fun `having in their name`(partialName: String): Set<SpacePort>
    infix fun `identified by`(id: String): SpacePort
}