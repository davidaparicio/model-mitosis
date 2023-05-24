package com.beyondxscratch.mandaloreexpress.domain.spacetrain

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.Schedule
import java.time.LocalDateTime.now
import kotlin.random.Random

private val departure = now()
        .plusDays(1)
        .withHour(10)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)

fun schedule(): Schedule = Schedule(departure, departure.plusDays(7))

fun randomSchedule(): Schedule = schedule().copy(arrival = departure.plusDays(Random.nextLong(8, 20)))
