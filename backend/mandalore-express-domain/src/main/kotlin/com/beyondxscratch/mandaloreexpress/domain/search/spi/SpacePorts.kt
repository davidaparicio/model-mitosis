package com.beyondxscratch.mandaloreexpress.domain.search.spi

import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort

interface SpacePorts {
    fun getAllSpacePorts(): Set<SpacePort>
}