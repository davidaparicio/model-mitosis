package org.craftsrecords.columbiadexpress.domain.sharedkernel

import java.util.UUID
import java.util.UUID.randomUUID

data class Fare(val id: UUID = randomUUID(), val comfortClass: ComfortClass, val price: Price)

typealias Fares = Set<Fare>