package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.search

import com.beyondxscratch.mandaloreexpress.domain.sharedkernel.ComfortClass
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.FareOption as DomainFareOption
import com.beyondxscratch.mandaloreexpress.domain.search.spacetrain.fare.FareOptions as DomainFareOptions
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.LinkBuilder
import java.util.UUID

@Resource
data class FareOption(
    private val id: UUID,
    val comfortClass: ComfortClass,
    val price: Price,
) : RepresentationModel<FareOption>()

typealias FareOptions = Set<FareOption>

fun DomainFareOptions.toResource(spaceTrainLink: LinkBuilder): FareOptions = this.map { it.toResource(spaceTrainLink) }.toSet()

fun DomainFareOption.toResource(spaceTrainLink: LinkBuilder? = null): FareOption {
    val fareOption = FareOption(id, comfortClass, price.toResource())

    spaceTrainLink?.let {
        fareOption.add(spaceTrainLink.slash("fareoptions")
                .slash("$id")
                .slash("select")
                .withRel("select"))
    }
    return fareOption
}