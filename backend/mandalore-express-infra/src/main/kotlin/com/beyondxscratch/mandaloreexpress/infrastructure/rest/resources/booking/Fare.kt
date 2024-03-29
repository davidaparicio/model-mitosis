package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.util.UUID
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Fare as DomainFare

@Resource
data class Fare(
    private val id: UUID,
    val comfortClass: ComfortClass,
    val price: Price,
) : RepresentationModel<Fare>()

fun DomainFare.toResource(): Fare = Fare(id, comfortClass, price.toResource())