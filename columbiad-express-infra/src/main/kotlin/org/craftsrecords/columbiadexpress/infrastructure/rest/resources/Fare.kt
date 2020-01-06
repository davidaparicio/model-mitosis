package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.search.ComfortClass
import org.craftsrecords.columbiadexpress.domain.search.Price
import org.craftsrecords.columbiadexpress.domain.search.Fare as DomainFare
import org.craftsrecords.columbiadexpress.domain.search.Fares as DomainFares

@Resource
class Fare(val comfortClass: ComfortClass, val price: Price)

typealias Fares = Set<Fare>

fun DomainFares.toResource(): Fares = this.map { it.toResource() }.toSet()

private fun DomainFare.toResource(): Fare = Fare(comfortClass, price)