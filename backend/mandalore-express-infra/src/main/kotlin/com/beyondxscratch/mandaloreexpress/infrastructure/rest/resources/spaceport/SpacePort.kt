package com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.spaceport

import com.beyondxscratch.mandaloreexpress.domain.spaceport.AstronomicalBody
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.controllers.SpacePortsController
import com.beyondxscratch.mandaloreexpress.infrastructure.rest.resources.Resource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.IanaLinkRelations.SELF
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.add
import com.beyondxscratch.mandaloreexpress.domain.spaceport.SpacePort as DomainSpacePort


const val SPACEPORTS: String = "spaceports"

@Resource
data class SpacePort(private val id: String, val name: String, val location: AstronomicalBody) :
    RepresentationModel<SpacePort>()

fun DomainSpacePort.toResource(): SpacePort {
    return SpacePort(id, name, location)
        .add(SpacePortsController::class) {
            linkTo { getSpacePortIdentifiedBy(id) } withRel SELF
        }
}

fun Set<DomainSpacePort>.asResourcesFor(partialNameRequest: String?): SpacePorts {
    return SpacePorts.of(map(DomainSpacePort::toResource)).add(SpacePortsController::class) {
        partialNameRequest?.let {
            linkTo { getSpacePorts(it) } withRel SELF
            linkTo { getSpacePorts(null) } withRel SPACEPORTS
        } ?: linkTo { getSpacePorts(null) } withRel SELF

    }
}

typealias SpacePorts = CollectionModel<SpacePort>