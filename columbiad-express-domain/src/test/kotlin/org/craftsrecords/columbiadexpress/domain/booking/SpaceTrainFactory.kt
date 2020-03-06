package org.craftsrecords.columbiadexpress.domain.booking

import org.craftsrecords.columbiadexpress.domain.sharedkernel.fare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.randomFare
import org.craftsrecords.columbiadexpress.domain.sharedkernel.randomSchedule
import org.craftsrecords.columbiadexpress.domain.sharedkernel.schedule
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import org.craftsrecords.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import org.craftsrecords.columbiadexpress.domain.spaceport.spacePort
import kotlin.random.Random.Default.nextLong

fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        originId = spacePort(EARTH).id,
        destinationId = spacePort(MOON).id,
        schedule = schedule(),
        fare = fare())

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                number = nextLong(1, 1000).toString(),
                schedule = randomSchedule(),
                fare = randomFare())