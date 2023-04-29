package com.beyondxscratch.mandaloreexpress.domain.api

import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.spi.SpacePorts


interface RetrieveSpacePorts {
    val spacePorts: SpacePorts
    infix fun `having in their name`(partialName: String): Set<SpacePort>
    infix fun `identified by`(id: String): SpacePort
}