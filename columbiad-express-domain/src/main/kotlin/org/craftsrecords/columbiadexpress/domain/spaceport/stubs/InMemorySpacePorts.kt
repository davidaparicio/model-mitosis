package org.craftsrecords.columbiadexpress.domain.spaceport.stubs

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts

class InMemorySpacePorts : SpacePorts {

    private val spacePorts: Set<SpacePort> = setOf(SpacePort(name = "Vostochny Cosmodrome", location = AstronomicalBody.EARTH), SpacePort(name = "Baikonur Cosmodrome", location = AstronomicalBody.EARTH), SpacePort(name = "Cape Canaveral Air Force Station", location = AstronomicalBody.EARTH))

    override fun getAllSpacePorts(): Set<SpacePort> {
        return spacePorts.toSet()
    }
}