package com.beyondxscratch.mandaloreexpress.domain.spaceport

data class SpacePort(val id: String, val name: String, val location: Planet) {

    infix fun `has a name containing`(partialName: String): Boolean {
        return name.contains(partialName, true)
    }

    infix fun `is not on the same planet than`(otherSpacePort: SpacePort): Boolean =
            this.location != otherSpacePort.location
}