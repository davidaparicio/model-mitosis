package org.craftsrecords.columbiadexpress.domain.search.criteria

fun oneWayCriteria() = criteria()
fun roundTripCriteria() = Criteria(listOf(journey(), inboundOf(journey())))
fun criteria(): Criteria = Criteria(listOf(journey()))
fun randomCriteria(): Criteria = Criteria(listOf(randomJourney()))