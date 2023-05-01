package com.beyondxscratch.mandaloreexpress.domain

import java.util.UUID

data class Fare(val id: UUID = UUID.randomUUID(), val comfortClass: ComfortClass, val price: Price)

typealias Fares = Set<Fare>