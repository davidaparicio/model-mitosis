package org.craftsrecords.columbiadexpress.domain.search

//TODO add id for selection
data class Fare(val comfortClass: ComfortClass, val price: Price)

typealias Fares = Set<Fare>