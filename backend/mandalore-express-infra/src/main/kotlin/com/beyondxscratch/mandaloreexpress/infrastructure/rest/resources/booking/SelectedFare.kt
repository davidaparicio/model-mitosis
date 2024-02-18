package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.booking

import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.ComfortClass
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import java.util.UUID
import com.beyondxscratch.mandaloreexpress.domain.booking.spacetrain.fare.SelectedFare as DomainSelectedFare

@Resource
data class SelectedFare(
    private val id: UUID,
    val comfortClass: ComfortClass,
    val price: Price,
) : RepresentationModel<SelectedFare>()

fun DomainSelectedFare.toResource(): SelectedFare {
    return SelectedFare(id, comfortClass, price.toResource())
}