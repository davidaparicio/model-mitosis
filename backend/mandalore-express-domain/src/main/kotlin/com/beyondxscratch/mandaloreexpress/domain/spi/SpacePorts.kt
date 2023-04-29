package com.beyondxscratch.mandaloreexpress.domain.spi

import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort

interface SpacePorts {
    fun getAllSpacePorts(): Set<SpacePort>
}