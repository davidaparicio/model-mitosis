package com.beyondxscratch.columbiadexpress.domain.api

import com.beyondxscratch.columbiadexpress.domain.spaceport.SpacePort
import com.beyondxscratch.columbiadexpress.domain.spi.SpacePorts


interface RetrieveSpacePorts {
    val spacePorts: SpacePorts
    infix fun `having in their name`(partialName: String): Set<SpacePort>
    infix fun `identified by`(id: String): SpacePort
}