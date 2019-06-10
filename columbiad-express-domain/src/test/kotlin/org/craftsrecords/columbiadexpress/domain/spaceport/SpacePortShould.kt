package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.columbiadexpress.domain.EqualityShould
import org.junit.jupiter.api.DisplayName
import java.util.*

@DisplayName("SpacePort should")
class SpacePortShould : EqualityShould<SpacePort> {
    override fun createValue(): SpacePort = SpacePort(id = UUID.fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), name = "name", location = AstronomicalBody.EARTH)

    override fun createAnotherValue(): SpacePort = SpacePort(id = UUID.fromString("9229d410-c1ff-4abe-b47a-328cbd4a78bf"), name = "other", location = AstronomicalBody.MOON)
}