package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.values
import org.craftsrecords.columbiadexpress.domain.stubs.InMemorySpacePorts
import java.util.UUID.randomUUID

private val spacePorts = InMemorySpacePorts()
fun spacePort(): SpacePort = spacePort(EARTH)
fun randomSpacePort(): SpacePort = SpacePort(id = randomUUID().toString(), name = randomUUID().toString(), location = values().random())
fun randomSpacePort(astronomicalBody: AstronomicalBody): SpacePort = spacePorts.getAllSpacePorts().filter { it.location == astronomicalBody }.drop(1).random()
fun spacePort(astronomicalBody: AstronomicalBody): SpacePort {
    return spacePorts.getAllSpacePorts().first { it.location == astronomicalBody }
}
