package org.craftsrecords.columbiadexpress.domain.search.criteria

fun criteria(): Criteria = Criteria(listOf(journey()))
fun randomCriteria(): Criteria = Criteria(listOf(randomJourney()))