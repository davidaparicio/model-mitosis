package org.craftsrecords.columbiadexpress.domain.search

import java.time.LocalDateTime

data class SpaceTrain (val departureSchedule : LocalDateTime, val arrivalSchedule : LocalDateTime, val fares : Set<Fare>)