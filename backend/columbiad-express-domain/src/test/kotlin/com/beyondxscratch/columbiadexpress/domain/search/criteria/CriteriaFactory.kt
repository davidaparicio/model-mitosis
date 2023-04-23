package com.beyondxscratch.columbiadexpress.domain.search.criteria

fun oneWayCriteria() = criteria()
fun roundTripCriteria() = Criteria(listOf(outboundJourney(), inboundOf(outboundJourney())))
fun criteria(): Criteria = Criteria(listOf(outboundJourney()))
fun randomCriteria(): Criteria = Criteria(listOf(randomJourney()))