package org.craftsrecords.columbiadexpress.domain.spaceport.stubs

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts
import java.util.UUID.nameUUIDFromBytes

@Stub
class InMemorySpacePorts : SpacePorts {

    private val spacePorts: Set<SpacePort> =
            setOf(SpacePort(id = nameUUIDFromBytes("1".toByteArray()), name = "Vostochny Cosmodrome", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("2".toByteArray()), name = "Baikonur Cosmodrome", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("3".toByteArray()), name = "Cape Canaveral Air Force Station", location = EARTH))

    override fun getAllSpacePorts(): Set<SpacePort> {
        return spacePorts.toSet()
    }
}