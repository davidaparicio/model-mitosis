package com.beyondxscratch.columbiadexpress.domain.booking

import com.beyondxscratch.columbiadexpress.domain.sharedkernel.fare
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.randomFare
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.randomSchedule
import com.beyondxscratch.columbiadexpress.domain.sharedkernel.schedule
import com.beyondxscratch.columbiadexpress.domain.spaceport.AstronomicalBody.EARTH
import com.beyondxscratch.columbiadexpress.domain.spaceport.AstronomicalBody.MOON
import com.beyondxscratch.columbiadexpress.domain.spaceport.spacePort
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