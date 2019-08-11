package org.craftsrecords.columbiadexpress.domain.spaceport

import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.values
import java.util.*

fun spacePort(): SpacePort = spacePort(EARTH)
fun randomSpacePort(): SpacePort = SpacePort(name = UUID.randomUUID().toString(), location = values().random())
fun spacePort(astronomicalBody: AstronomicalBody): SpacePort {
    return SpacePort(id = UUID.fromString("fa9f2371-5b13-40a1-bd18-42db3371f073"), name = "name", location = astronomicalBody)
}
