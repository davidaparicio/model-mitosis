package org.craftsrecords.columbiadexpress.domain.search

import org.craftsrecords.columbiadexpress.domain.search.ComfortClass.FIRST

fun fare() : Fare = Fare(FIRST, price())
fun randomFare() : Fare = Fare(ComfortClass.values().random(), randomPrice())