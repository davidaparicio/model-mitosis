package org.craftsrecords.columbiadexpress.domain.spaceport.stubs

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.SpacePort
import org.craftsrecords.columbiadexpress.domain.spaceport.spi.SpacePorts
import java.util.UUID.nameUUIDFromBytes

@Stub
class InMemorySpacePorts : SpacePorts {

    private val spacePorts: Set<SpacePort> =
            setOf(SpacePort(id = nameUUIDFromBytes("Vostochny".toByteArray()), name = "Vostochny Cosmodrome", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("Baikonur".toByteArray()), name = "Baikonur Cosmodrome", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("CC".toByteArray()), name = "Cape Canaveral Air Force Station", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("VBG".toByteArray()), name = "Vandenberg Air Force Base", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("KSC".toByteArray()), name = "John F. Kennedy Space Center", location = EARTH),
                    SpacePort(id = nameUUIDFromBytes("CSG".toByteArray()), name = "Centre Spatial Guyanais", location = EARTH),

                    SpacePort(id = nameUUIDFromBytes("Peary".toByteArray()), name = "Peary Crater", location = MOON),
                    SpacePort(id = nameUUIDFromBytes("Malapert".toByteArray()), name = "Malapert Mountain", location = MOON),
                    SpacePort(id = nameUUIDFromBytes("Mare Cognitum".toByteArray()), name = "Mare Cognitum", location = MOON),
                    SpacePort(id = nameUUIDFromBytes("Aitken basin".toByteArray()), name = "South Pole–Aitken basin", location = MOON))

    override fun getAllSpacePorts(): Set<SpacePort> {
        return spacePorts.toSet()
    }
}