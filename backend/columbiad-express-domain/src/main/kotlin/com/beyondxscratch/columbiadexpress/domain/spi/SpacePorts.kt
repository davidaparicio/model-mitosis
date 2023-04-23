package com.beyondxscratch.columbiadexpress.domain.spi

import com.beyondxscratch.columbiadexpress.domain.spaceport.SpacePort

interface SpacePorts {
    fun getAllSpacePorts(): Set<SpacePort>
}