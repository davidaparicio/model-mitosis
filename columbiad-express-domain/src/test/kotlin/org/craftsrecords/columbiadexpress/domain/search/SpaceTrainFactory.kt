package org.craftsrecords.columbiadexpress.domain.search

import java.time.LocalDateTime.now

fun spaceTrain(): SpaceTrain = SpaceTrain(now().plusDays(1), now().plusWeeks(1), setOf(fare()))
fun randomSpaceTrain(): SpaceTrain = spaceTrain().copy(fares = setOf(randomFare(), randomFare()))