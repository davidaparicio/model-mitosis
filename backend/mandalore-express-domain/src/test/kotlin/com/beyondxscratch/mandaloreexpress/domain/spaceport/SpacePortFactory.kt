package com.beyondxscratch.mandaloreexpress.domain.spaceport

import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.values
import com.beyondxscratch.mandaloreexpress.domain.search.spi.InMemorySpacePorts
import java.util.UUID.randomUUID

private val spacePorts = InMemorySpacePorts()
fun spacePort(): SpacePort = spacePort(CORUSCANT)
fun randomSpacePort(): SpacePort = SpacePort(id = randomUUID().toString(), name = randomUUID().toString(), location = values().random())
fun randomSpacePort(planet: Planet): SpacePort = spacePorts.getAllSpacePorts().filter { it.location == planet }.drop(1).random()
fun spacePort(planet: Planet): SpacePort {
    return spacePorts.getAllSpacePorts().first { it.location == planet }
}
