package com.beyondxscratch.mandaloreexpress.domain.criteria

import com.beyondxscratch.mandaloreexpress.domain.search.criteria.Criteria

fun oneWayCriteria() = criteria()
fun roundTripCriteria() = Criteria(listOf(outboundJourney(), inboundOf(outboundJourney())))
fun criteria(): Criteria = Criteria(listOf(outboundJourney()))
fun randomCriteria(): Criteria = Criteria(listOf(randomJourney()))