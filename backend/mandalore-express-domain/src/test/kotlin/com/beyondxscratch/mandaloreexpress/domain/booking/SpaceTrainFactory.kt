package com.beyondxscratch.mandaloreexpress.domain.booking

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.fare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.randomFare
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.randomSchedule
import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.schedule
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.CORUSCANT
import com.beyondxscratch.mandaloreexpress.domain.spaceport.Planet.MANDALORE
import com.beyondxscratch.mandaloreexpress.domain.spaceport.spacePort
import kotlin.random.Random.Default.nextLong

fun spaceTrain(): SpaceTrain = SpaceTrain(
        number = "6127",
        originId = spacePort(CORUSCANT).id,
        destinationId = spacePort(MANDALORE).id,
        schedule = schedule(),
        fare = fare())

fun randomSpaceTrain(): SpaceTrain = spaceTrain()
        .copy(
                number = nextLong(1, 1000).toString(),
                schedule = randomSchedule(),
                fare = randomFare())