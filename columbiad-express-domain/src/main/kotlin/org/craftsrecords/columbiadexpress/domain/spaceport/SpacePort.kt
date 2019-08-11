package org.craftsrecords.columbiadexpress.domain.spaceport

import java.util.*
import java.util.UUID.randomUUID

data class SpacePort(val id: UUID = randomUUID(), val name: String, val location: AstronomicalBody) {
    infix fun `has a name containing`(partialName: String): Boolean {
        return name.contains(partialName, true)
    }
}