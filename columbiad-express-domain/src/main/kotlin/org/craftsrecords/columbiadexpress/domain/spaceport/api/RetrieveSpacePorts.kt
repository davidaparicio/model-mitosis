package org.craftsrecords.columbiadexpress.domain.spaceport.api

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort

interface RetrieveSpacePorts {
    infix fun `having in their name`(partialName: String): Set<SpacePort>
}