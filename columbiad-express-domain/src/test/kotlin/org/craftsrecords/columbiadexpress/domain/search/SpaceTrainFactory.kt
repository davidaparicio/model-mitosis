package org.craftsrecords.columbiadexpress.domain.search

import java.time.LocalDateTime.now

private val departureSchedule =
        now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

fun spaceTrain(): SpaceTrain = SpaceTrain(departureSchedule, departureSchedule.plusWeeks(1), setOf(fare()))
fun randomSpaceTrain(): SpaceTrain = spaceTrain().copy(fares = setOf(randomFare(), randomFare()))