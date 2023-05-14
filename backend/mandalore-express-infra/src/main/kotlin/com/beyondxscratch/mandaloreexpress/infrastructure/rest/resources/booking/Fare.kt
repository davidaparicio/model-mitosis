package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.sharedkernel.Price
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.sharedkernel.toResource
import org.springframework.hateoas.RepresentationModel
import java.util.*
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.Fare as DomainFare

@Resource
data class Fare(
    private val id: UUID,
    val comfortClass: ComfortClass,
    val price: Price,
) : RepresentationModel<Fare>()

fun DomainFare.toResource(): Fare = Fare(id, comfortClass, price.toResource())