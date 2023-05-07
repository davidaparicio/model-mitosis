package com.beyondxscratch.mandaloreexpress.domain.search.spi

import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort

fun interface SpacePorts {
    fun getAllSpacePorts(): Set<SpacePort>
}