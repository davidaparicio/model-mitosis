package org.craftsrecords.columbiadexpress.infrastructure.rest.resources

import org.craftsrecords.columbiadexpress.domain.search.ComfortClass
import org.craftsrecords.columbiadexpress.domain.search.Price
import org.springframework.hateoas.RepresentationModel
import java.util.UUID

@Resource
data class Fare(private val id: UUID, val comfortClass: ComfortClass, val price: Price) : RepresentationModel<Fare>()

typealias Fares = Set<Fare>