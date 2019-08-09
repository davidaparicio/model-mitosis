package org.craftsrecords.columbiadexpress.domain.spaceport.api

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import java.util.*

interface RetrieveSpacePorts {
    infix fun `having in their name`(partialName: String): Set<SpacePort>
    infix fun `identified by`(id: UUID): SpacePort
}