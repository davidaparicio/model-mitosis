package org.craftsrecords.columbiadexpress.domain.spi

import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort

interface SpacePorts {
    fun getAllSpacePorts(): Set<SpacePort>
}