package org.craftsrecords.columbiadexpress.domain.spaceport

import java.util.*

class SpacePort(val id: UUID = UUID.randomUUID(), val name: String, val location: AstronomicalBody) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpacePort

        if (id != other.id) return false

        return true
    }

    infix fun `has a name containing`(partialName: String): Boolean {
        return name.contains(partialName, true)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}