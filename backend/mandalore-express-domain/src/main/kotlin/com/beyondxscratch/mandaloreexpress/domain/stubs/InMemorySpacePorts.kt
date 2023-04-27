package com.beyondxscratch.mandaloreexpress.domain.stubs

import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.NEVARRO
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.TATOOINE
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.spi.SpacePorts
import java.util.UUID.nameUUIDFromBytes

@Stub
class InMemorySpacePorts : SpacePorts {

    private val spacePorts: Set<SpacePort> =
            setOf(SpacePort(id = "Uscru".toUUIDString(), name = "Uscru District", location = CORUSCANT),
                    SpacePort(id = "CoCo".toUUIDString(), name = "CoCo Town", location = CORUSCANT),
                    SpacePort(id = "Nevarro".toUUIDString(), name = "Nevarro City", location = NEVARRO),
                    SpacePort(id = "Mos Eisley".toUUIDString(), name = "Mos Eisley", location = TATOOINE),
                    SpacePort(id = "Mos Espa".toUUIDString(), name = "Mos Espa", location = TATOOINE),

                    SpacePort(id = "Keldabe".toUUIDString(), name = "Keldabe", location = MANDALORE),
                    SpacePort(id = "Sundari".toUUIDString(), name = "Sundari", location = MANDALORE))

    override fun getAllSpacePorts(): Set<SpacePort> {
        return spacePorts.toSet()
    }

    private fun String.toUUIDString(): String = nameUUIDFromBytes(this.toByteArray()).toString()
}