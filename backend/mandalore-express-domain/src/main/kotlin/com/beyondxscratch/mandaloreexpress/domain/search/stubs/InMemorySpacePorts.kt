package com.beyondxscratch.mandaloreexpress.domain.search.stubs

import com.beyondxscratch.mandaloreexpress.annotations.Stub
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.NEVARRO
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.Planet.TATOOINE
import com.beyondxscratch.mandaloreexpress.domain.search.spaceport.SpacePort
import com.beyondxscratch.mandaloreexpress.domain.search.spi.SpacePorts
import java.util.UUID.nameUUIDFromBytes

@Stub
class InMemorySpacePorts : SpacePorts {

    private val spacePorts: Set<SpacePort> =
            setOf(
                SpacePort(id = "Uscru".toUUIDString(), name = "Uscru District", location = CORUSCANT),
                    SpacePort(id = "CoCo".toUUIDString(), name = "CoCo Town", location = CORUSCANT),
                    SpacePort(id = "Nevarro".toUUIDString(), name = "Nevarro City", location = NEVARRO),
                    SpacePort(id = "Mos Eisley".toUUIDString(), name = "Mos Eisley", location = TATOOINE),
                    SpacePort(id = "Mos Espa".toUUIDString(), name = "Mos Espa", location = TATOOINE),

                    SpacePort(id = "Keldabe".toUUIDString(), name = "Keldabe", location = MANDALORE),
                    SpacePort(id = "Sundari".toUUIDString(), name = "Sundari", location = MANDALORE)
            )

    override fun getAllSpacePorts(): Set<SpacePort> {
        return spacePorts.toSet()
    }

    private fun String.toUUIDString(): String = nameUUIDFromBytes(this.toByteArray()).toString()
}