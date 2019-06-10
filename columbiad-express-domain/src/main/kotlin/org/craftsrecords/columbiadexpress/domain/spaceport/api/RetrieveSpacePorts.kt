package org.craftsrecords.columbiadexpress.domain.spaceport.api

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort

interface RetrieveSpacePorts {
    infix fun `having their name containing`(partialName: String): Set<SpacePort>
}